/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mdemangler.naming;

import mdemangler.*;

/**
 * This class represents a qualified name (wiki page parlance) within a name of a
 *  Microsoft mangled symbol. Note that it is slightly different from MDQualifiedName
 *  in that it has an MDBasicName as its first component.
 */
public class MDQualifiedBasicName extends MDParsableItem {
	private MDBasicName basicName;
	private MDQualification qualification;

	public MDQualifiedBasicName(MDMang dmang) {
		super(dmang);
		basicName = new MDBasicName(dmang);
		qualification = new MDQualification(dmang);
	}

	public boolean isTypeCast() {
		return basicName.isTypeCast();
	}

	public boolean isConstructor() {
		if (basicName == null) {
			return false;
		}
		return basicName.isConstructor();
	}

	public boolean isDestructor() {
		if (basicName == null) {
			return false;
		}
		return basicName.isDestructor();
	}

	/**
	 * Returns the RTTI number:{0-4, or -1 if not an RTTI}
	 * @return int RTTI number:{0-4, or -1 if not an RTTI}
	 */
	public int getRTTINumber() {
		if (basicName != null) {
			return basicName.getRTTINumber();
		}
		return -1;
	}

	public boolean isString() {
		if (basicName == null) {
			return false;
		}
		return basicName.isString();
	}

	@Override
	public void insert(StringBuilder builder) {
		basicName.insert(builder);
		if (qualification.hasContent()) {
			dmang.insertString(builder, "::");
			qualification.insert(builder);
		}
	}

	public MDBasicName getBasicName() {
		return basicName;
	}

	public MDQualification getQualification() {
		return qualification;
	}

	public void setNameModifier(String nameModifier) {
		basicName.setNameModifier(nameModifier);
	}

	@Override
	protected void parseInternal() throws MDException {
		basicName.parse();
		qualification.parse();
		if (basicName.isConstructor()) {
			StringBuilder builder = new StringBuilder();
			qualification.insertHeadQualifier(builder);
			basicName.setName(builder.toString());
		}
		else if (basicName.isDestructor()) {
			StringBuilder builder = new StringBuilder();
			qualification.insertHeadQualifier(builder);
			dmang.insertString(builder, "~");
			basicName.setName(builder.toString());
		}
	}
}

/******************************************************************************/
/******************************************************************************/
