XModifier
=========

*This project is just a hobby project, please feel free to copy, change or use it.*

The purpose of this project is very simple:  **Use XPATH to edit XML or create XML**

XPATH is used for querying XML,
but sometimes you may want use it to edit/create XML:

#### examples

<table style="font-size:80%">
<tr><th>XPath</th><th>Value</th><th>Desc</th></tr>
<tr><td>/ns:root/ns:element1                                         </td><td>       </td><td>add &lt;ns:element1/>                               </td></tr>
<tr><td>/ns:root/ns:element2[@attr=1]                                </td><td>       </td><td>add &lt;ns:element2 attr="1"/>                      </td></tr>
<tr><td>/ns:root/ns:element2/@attr                                   </td><td>1      </td><td>add &lt;ns:element2 attr="1"/>                      </td></tr>
<tr><td>/ns:root/ns:element1/ns:element11                            </td><td>       </td><td>add &lt;ns:element11/>                              </td></tr>
<tr><td>/ns:root/ns:element3                                         </td><td>TEXT   </td><td>add &lt;ns:element3>TEXT&lt;/ns:element3>              </td></tr>
<tr><td>/ns:root/ns:element1[ns:element12]/ns:element13              </td><td>       </td><td>add &lt;ns:element13/>                              </td></tr>
<tr><td>//PersonList/Person[2]/Name                                  </td><td>NewName</td><td>set the second Person node's Name Text           </td></tr>
<tr><td>//PersonList/Person[2]/Name/text()                           </td><td>NewName</td><td>set the second Person node's Name Text           </td></tr>
<tr><td>//PersonList/Person[1]/Name(:delete)                         </td><td>       </td><td>delete this Name node                            </td></tr>
<tr><td>//PersonList/Person(:add)/Name                               </td><td>NewName</td><td>always add a new Person node                      </td></tr>
<tr><td>//PersonList/Person(:insertBefore(Person[Name='Name2']))/Name</td><td>NewName</td><td>add a new Person node before Person named "Name2"</td></tr>
</table>

Special XModifier mark
all special marks are start with (: and end with )
they are not part of standard XPATH, use them only when you have to.
<table style="font-size:80%">
<tr><th>Mark</th><th>usage</th></tr>
<tr><td>(:add)                </td><td>always add new element (by default only add new element when not exist)                          </td></tr>
<tr><td>(:delete)             </td><td>delete an element                                                                                </td></tr>
<tr><td>(:insertBefore(XPATH))</td><td>always add new element, and control the position of it, (by default only append to last element) </td></tr>
</table>


# Code example:

#### Create new xml:

```java
		Document document = createDocument(); //empty document
		XModifier modifier = new XModifier(document);
		modifier.setNamespace("ns", "http://localhost");
		// create an empty element
		modifier.addModify("/ns:root/ns:element1");
		// create an element with attribute
		modifier.addModify("/ns:root/ns:element2[@attr=1]");
		// append an new element to existing element1
		modifier.addModify("/ns:root/ns:element1/ns:element11");
		// create an element with text
		modifier.addModify("/ns:root/ns:element3", "TEXT");
		modifier.modify();
```
result xml:
```xml
		<root xmlns="http://localhost">
			<element1>
				<element11/>
			</element1>
			<element2 attr="1"/>
			<element3>TEXT</element3>
		</root>
```
#### Modify exist xml:

original xml:
```xml
		<root xmlns="http://localhost">
			<element1>
				<element11></element11>
			</element1>
			<element1>
				<element12></element12>
			</element1>
			<element2></element2>
			<element3></element3>
		</root>
```
```java
		Document document = readDocument("modify.xml");
		XModifier modifier = new XModifier(document);
		modifier.setNamespace("ns", "http://localhost");
		modifier.addModify("/ns:root/ns:element1[ns:element12]/ns:element13");
		modifier.modify();
```
result xml:
```xml
		<root xmlns="http://localhost">
			<element1>
				<element11/>
			</element1>
			<element1>
				<element12/>
				<element13/>
			</element1>
			<element2/>
			<element3/>
		</root>
```
a new element ns:element13 is being added
