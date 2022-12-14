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
package org.apache.juneau.objecttools;

import static org.apache.juneau.assertions.Assertions.*;
import static org.junit.Assert.*;
import static org.junit.runners.MethodSorters.*;

import java.util.*;

import org.apache.juneau.*;
import org.junit.*;

/**
 * Tests the PojoPaginator class.
 */
@FixMethodOrder(NAME_ASCENDING)
public class ObjectPaginator_Test {

	ObjectPaginator op = ObjectPaginator.create();
	BeanSession bs = BeanContext.DEFAULT_SESSION;

	//-----------------------------------------------------------------------------------------------------------------
	// Null input
	//-----------------------------------------------------------------------------------------------------------------

	@Test
	public void a01_nullInput() {
		assertNull(op.run(bs, null, null));
	}

	@Test
	public void a02_nonCollectionInput() {
		assertObject(op.run(bs, "foo", PageArgs.create(1, 3))).is("foo");
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Arrays
	//-----------------------------------------------------------------------------------------------------------------

	@Test
	public void b01_arrays_basic() {
		Object in = new int[]{1,2,3};
		assertList(op.run(in, 0, 3)).isHas(1,2,3);
		assertList(op.run(in, 1, 3)).isHas(2,3);
		assertList(op.run(in, 1, 1)).isHas(2);
		assertList(op.run(in, 4, 1)).isHas();
		assertList(op.run(in, 0, 0)).isHas();

		in = new String[]{"1","2","3"};
		assertList(op.run(in, 1, 1)).isHas("2");

		in = new boolean[]{false,true,false};
		assertList(op.run(in, 1, 1)).isHas(true);

		in = new byte[]{1,2,3};
		assertList(op.run(in, 1, 1)).isHas((byte)2);

		in = new char[]{'1','2','3'};
		assertList(op.run(in, 1, 1)).isHas('2');

		in = new double[]{1,2,3};
		assertList(op.run(in, 1, 1)).isHas((double)2);

		in = new float[]{1,2,3};
		assertList(op.run(in, 1, 1)).isHas((float)2);

		in = new long[]{1,2,3};
		assertList(op.run(in, 1, 1)).isHas((long)2);

		in = new short[]{1,2,3};
		assertList(op.run(in, 1, 1)).isHas((short)2);
	}

	//-----------------------------------------------------------------------------------------------------------------
	// Collections
	//-----------------------------------------------------------------------------------------------------------------

	@Test
	public void c01_collections_basic() {
		Object in = Arrays.asList(1,2,3);
		assertList(op.run(in, 0, 3)).isHas(1,2,3);
		assertList(op.run(in, 1, 3)).isHas(2,3);
		assertList(op.run(in, 1, 1)).isHas(2);
		assertList(op.run(in, 4, 1)).isHas();
		assertList(op.run(in, 0, 0)).isHas();

		in = new LinkedHashSet<>(Arrays.asList(1,2,3));
		assertList(op.run(in, 0, 3)).isHas(1,2,3);
		assertList(op.run(in, 1, 3)).isHas(2,3);
		assertList(op.run(in, 1, 1)).isHas(2);
		assertList(op.run(in, 4, 1)).isHas();
		assertList(op.run(in, 0, 0)).isHas();
}
}
