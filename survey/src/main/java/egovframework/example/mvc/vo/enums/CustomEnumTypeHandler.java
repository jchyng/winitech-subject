package egovframework.example.mvc.vo.enums;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * MyBatis에서 Enum에 값을 매핑하기 위한 핸들러
 */
public class CustomEnumTypeHandler<E extends Enum<E>> extends EnumTypeHandler<E> {

    private final Class<E> type;

    public CustomEnumTypeHandler(Class<E> type) {
        super(type);
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (rs.wasNull()) {
            return null;
        } else {
            return Enum.valueOf(type, value.toUpperCase());
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (rs.wasNull()) {
            return null;
        } else {
            return Enum.valueOf(type, value.toUpperCase());
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (cs.wasNull()) {
            return null;
        } else {
            return Enum.valueOf(type, value.toUpperCase());
        }
    }
}