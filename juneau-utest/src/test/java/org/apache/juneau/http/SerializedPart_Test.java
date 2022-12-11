// ***************************************************************************************************************************
// * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.  See the NOTICE file *
// * distributed with this work for additional information regarding copyright ownership.  The ASF licenses this file        *
// * to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance            *
// * with the License.  You may obtain a copy of the License at                                                              *
// *                                                                                                                         *
// *  http://www.apache.org/licenses/LICENSE-2.0                                                                             *
// *                                                                                                                         *
// * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an  *
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the        *
// * specific language governing permissions and limitations under the License.                                              *
// ***************************************************************************************************************************
package org.apache.juneau.http;

import static org.junit.runners.MethodSorters.*;
import static org.apache.juneau.httppart.HttpPartSchema.*;
import static org.apache.juneau.http.HttpParts.*;

import org.apache.juneau.http.part.*;
import org.apache.juneau.httppart.*;
import org.apache.juneau.oapi.*;
import org.apache.juneau.serializer.*;

import static org.apache.juneau.assertions.Assertions.*;
import static org.apache.juneau.httppart.HttpPartType.*;
import static org.apache.juneau.internal.CollectionUtils.*;
import static org.apache.juneau.httppart.HttpPartDataType.*;

import org.junit.*;

@FixMethodOrder(NAME_ASCENDING)
public class SerializedPart_Test {

	private static final OpenApiSerializerSession OAPI_SESSION = OpenApiSerializer.DEFAULT.getSession();
	private static final OpenApiSerializer OAPI_SERIALIZER = OpenApiSerializer.DEFAULT;

	@Test
	public void a01_basic() throws Exception {
		SerializedPart x1 = new SerializedPart("Foo",alist("bar","baz"),HEADER,OAPI_SESSION,T_ARRAY_PIPES,true);
		assertString(x1).is("Foo=bar|baz");
	}

	@Test
	public void a02_type() throws Exception {
		SerializedPart x1 = serializedPart("Foo",2).type(HEADER).serializer(OAPI_SERIALIZER).schema(schema(INTEGER).maximum(1).build());
		assertThrown(()->x1.toString()).asMessage().is("Validation error on request HEADER part 'Foo'='2'");
	}

	@Test
	public void a03_serializer() throws Exception {
		SerializedPart x1 = serializedPart("Foo",alist("bar","baz")).serializer((HttpPartSerializer)null);
		assertString(x1.getValue()).is("[bar, baz]");
		SerializedPart x2 = serializedPart("Foo",alist("bar","baz")).serializer((HttpPartSerializer)null).serializer(OAPI_SERIALIZER);
		assertString(x2.getValue()).is("bar,baz");
		SerializedPart x3 = serializedPart("Foo",alist("bar","baz")).serializer(OAPI_SERIALIZER).serializer((HttpPartSerializerSession)null);
		assertString(x3.getValue()).is("[bar, baz]");
		SerializedPart x4 = serializedPart("Foo",alist("bar","baz")).serializer(OAPI_SERIALIZER).copyWith(null,null);
		assertString(x4.getValue()).is("bar,baz");
		SerializedPart x5 = serializedPart("Foo",alist("bar","baz")).copyWith(OAPI_SERIALIZER.getPartSession(),null);
		assertString(x5.getValue()).is("bar,baz");
	}

	@Test
	public void a04_skipIfEmpty() throws Exception {
		SerializedPart x1 = serializedPart("Foo",null).skipIfEmpty();
		assertString(x1.getValue()).isNull();
		SerializedPart x2 = serializedPart("Foo","").skipIfEmpty();
		assertString(x2.getValue()).isNull();
		SerializedPart x3 = serializedPart("Foo","").schema(schema(STRING)._default("bar").build()).serializer(OAPI_SERIALIZER).skipIfEmpty();
		assertThrown(()->x3.getValue()).asMessages().isContains("Empty value not allowed.");
	}

	@Test
	public void a05_getValue_defaults() throws Exception {
		SerializedPart x1 = serializedPart("Foo",null).schema(schema(INTEGER)._default("1").build()).serializer(OAPI_SESSION);
		assertString(x1.getValue()).is("1");

		SerializedPart x2 = serializedPart("Foo",null).schema(schema(STRING).required().allowEmptyValue().build()).serializer(OAPI_SESSION);
		assertString(x2.getValue()).isNull();

		SerializedPart x3 = serializedPart("Foo",null).schema(schema(STRING).required(false).build()).serializer(OAPI_SESSION);
		assertString(x3.getValue()).isNull();

		SerializedPart x4 = serializedPart("Foo",null).schema(schema(STRING).required().build()).serializer(OAPI_SESSION);
		assertThrown(()->x4.getValue()).asMessages().isContains("Required value not provided.");

		SerializedPart x5 = serializedPart("Foo",null).schema(schema(STRING).required().build()).serializer(new BadPartSerializerSession());
		assertThrown(()->x5.getValue()).asMessages().isContains("Bad");
	}

	private static class BadPartSerializerSession implements HttpPartSerializerSession {
		@Override
		public String serialize(HttpPartType type, HttpPartSchema schema, Object value) throws SerializeException, SchemaValidationException {
			throw new SerializeException("Bad");
		}
	}

	//------------------------------------------------------------------------------------------------------------------
	// Utility methods
	//------------------------------------------------------------------------------------------------------------------

	private HttpPartSchema.Builder schema(HttpPartDataType dataType) {
		return HttpPartSchema.create().type(dataType);
	}
}
