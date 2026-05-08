package com.yapp.ndgl.domain.common.lock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.lock.DistributedLockRepository;
import com.yapp.ndgl.lock.LockOptions;
import com.yapp.ndgl.lock.NamedLockOptions;

public class NamedLockRepository implements DistributedLockRepository {

    private final DataSource dataSource;

    public NamedLockRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void withLock(final LockOptions options, final Runnable task) {
        if (!(options instanceof NamedLockOptions(String key, int timeoutSeconds))) {
            throw new GlobalException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
        try (Connection conn = dataSource.getConnection()) {
            acquire(conn, key, timeoutSeconds);
            try {
                task.run();
            } finally {
                release(conn, key);
            }
        } catch (SQLException e) {
            throw new RuntimeException("분산락 처리 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public LockOptions createOptions(final String key, final int timeoutSeconds) {
        return NamedLockOptions.of(key, timeoutSeconds);
    }

    private void acquire(final Connection conn, final String key, final int timeoutSeconds) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT GET_LOCK(?, ?)")) {
            ps.setString(1, key);
            ps.setInt(2, timeoutSeconds);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                if (rs.getInt(1) != 1) {
                    throw new GlobalException(CommonErrorCode.LOCK_ACQUISITION_TIMEOUT);
                }
            }
        }
    }

    private void release(final Connection conn, final String key) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT RELEASE_LOCK(?)")) {
            ps.setString(1, key);
            ps.executeQuery();
        }
    }
}
