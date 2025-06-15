package de.hskl.swtp.carpooling.converter;

import de.hskl.swtp.carpooling.model.Position;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@Converter(autoApply = true)
public class PositionConverter implements AttributeConverter<Position, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(Position position) {
        if (position == null) return null;

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        buffer.putInt(0);          // SRID = 0
        buffer.put((byte) 1);      // Byte order = little endian
        buffer.putInt(1);          // Geometry type = 1 (Point)
        buffer.putDouble(position.getLongitude());
        buffer.putDouble(position.getLatitude());

        return buffer.array();
    }

    @Override
    public Position convertToEntityAttribute(byte[] bytes) {
        if (bytes == null || bytes.length != 25) return null;

        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        int srid = buffer.getInt();            // SRID (ignored here)
        byte byteOrder = buffer.get();         // byte order
        buffer.order(byteOrder == 1 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);

        int type = buffer.getInt();            // Geometry type
        if (type != 1) {
            throw new IllegalArgumentException("Unsupported geometry type: " + type);
        }

        double lon = buffer.getDouble();
        double lat = buffer.getDouble();

        return new Position(lon, lat);
    }
}
