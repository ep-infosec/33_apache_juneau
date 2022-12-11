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
package org.apache.juneau.utils;

import static org.apache.juneau.assertions.Assertions.*;
import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.*;

import org.junit.*;

@FixMethodOrder(NAME_ASCENDING)
public class Tuple3_Test {

	//------------------------------------------------------------------------------------------------------------------
	// Basic tests.
	//------------------------------------------------------------------------------------------------------------------
	@Test
	public void a01_basic() throws Exception {
		Tuple3<String,Integer,Integer> x = Tuple3.of("foo",1,2);
		assertString(x.getA()).is("foo");
		assertInteger(x.getB()).is(1);
		assertInteger(x.getC()).is(2);
	}

	@Test
	public void a02_equality() throws Exception {
		Tuple3<String,Integer,Integer> x1 = Tuple3.of("foo",1,2), x2 = Tuple3.of("foo",1,2), x3 = Tuple3.of(null,1,2), x4 = Tuple3.of("foo",null,2), x5 = Tuple3.of("foo",1,null);
		assertTrue(x1.equals(x2));
		assertEquals(x1.hashCode(), x2.hashCode());
		assertFalse(x1.equals(x3));
		assertNotEquals(x1.hashCode(), x3.hashCode());
		assertFalse(x1.equals(x4));
		assertNotEquals(x1.hashCode(), x4.hashCode());
		assertFalse(x1.equals(x5));
		assertNotEquals(x1.hashCode(), x5.hashCode());
	}
}
