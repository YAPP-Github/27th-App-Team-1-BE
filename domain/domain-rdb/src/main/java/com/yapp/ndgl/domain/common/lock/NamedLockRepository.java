package com.yapp.ndgl.domain.common.lock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.lock.DistributedLockRepository;
import com.yapp.ndgl.lock.LockOptions;
import com.yapp.ndgl.lock.LockTask;
import com.yapp.ndgl.lock.NamedLockOptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NamedLockRepository implements DistributedLockRepository {

    private static final String GET_LOCK_SQL = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK_SQL = "SELECT RELEASE_LOCK(?)";

    private final JdbcTemplate jdbcTemplate;

    public NamedLockRepository(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public <T> T withLock(final LockOptions options, final LockTask<T> task) throws Throwable {
        if (!(options instanceof NamedLockOptions(String key, int timeoutSeconds))) {
            log.error("지원하지 않는 LockOptions 타입입니다. type={}",
                options == null ? "null" : options.getClass().getName());
            throw new GlobalException(CommonErrorCode.INVALID_LOCK_OPTIONS);
        }

        try {
            return jdbcTemplate.execute((ConnectionCallback<T>) conn -> {
                acquire(conn, key, timeoutSeconds);
                try {
                    return task.execute();
                } catch (Throwable t) {
                    throw new TaskFailure(t);
                } finally {
                    release(conn, key);
                }
            });
        } catch (TaskFailure wrapper) {
            throw wrapper.getCause();
        } catch (DataAccessException e) {
            log.error("분산락 처리 중 SQL 오류가 발생했습니다. key={}", key, e);
            throw new GlobalException(CommonErrorCode.LOCK_EXECUTION_FAILED);
        }
    }

    @Override
    public LockOptions createOptions(final String key, final int timeoutSeconds) {
        return NamedLockOptions.of(key, timeoutSeconds);
    }

    private void acquire(final Connection conn, final String key, final int timeoutSeconds) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(GET_LOCK_SQL)) {
            ps.setString(1, key);
            ps.setInt(2, timeoutSeconds);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    log.error("GET_LOCK 결과가 비어있습니다. key={}", key);
                    throw new GlobalException(CommonErrorCode.LOCK_ACQUISITION_TIMEOUT);
                }
                Integer acquired = (Integer) rs.getObject(1);
                if (acquired == null || acquired != 1) {
                    log.warn("분산락 획득 실패. key={}, result={}", key, acquired);
                    throw new GlobalException(CommonErrorCode.LOCK_ACQUISITION_TIMEOUT);
                }
            }
        }
    }

    private void release(final Connection conn, final String key) {
        boolean leaked = false;
        try (PreparedStatement ps = conn.prepareStatement(RELEASE_LOCK_SQL)) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    leaked = true;
                    log.error("RELEASE_LOCK 결과가 비어있습니다. key={}", key);
                } else {
                    Integer released = (Integer) rs.getObject(1);
                    if (released == null || released != 1) {
                        leaked = true;
                        log.error("RELEASE_LOCK 실패. 락이 해제되지 않았습니다. key={}, result={}", key, released);
                    }
                }
            }
        } catch (SQLException e) {
            leaked = true;
            log.error("RELEASE_LOCK 실행 중 오류가 발생했습니다. key={}", key, e);
        }

        if (leaked) {
            abortConnection(conn, key);
        }
    }

    private void abortConnection(final Connection conn, final String key) {
        try {
            conn.abort(Runnable::run);
            log.warn("락 누수 방지를 위해 커넥션을 강제 종료했습니다. key={}", key);
        } catch (SQLException ex) {
            log.error("락 누수 방지용 커넥션 강제 종료에 실패했습니다. key={}", key, ex);
        }
    }

    private static final class TaskFailure extends RuntimeException {
        TaskFailure(final Throwable cause) {
            super(cause);
        }
    }
}
