package com.universum.common.configuration.json.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class NullKeySerializer extends StdSerializer<Object>{
	private static final long serialVersionUID = 7482686333295692433L;

	public NullKeySerializer() {
        this(null);
    }

    public NullKeySerializer(Class<Object> t) {
        super(t);
    }

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeFieldName("");
	}

}
