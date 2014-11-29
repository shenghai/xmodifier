xmodifier
=========
		The purpose of this project is very simple:
		use XPATH to edit XML or create XML
		XPATH is used for searching XML, 
		but sometimes you may want use it to edit/create XML:

		| XPath                                                         | Value   | Desc                                              |
		|---------------------------------------------------------------+---------+---------------------------------------------------|
		| /ns:root/ns:element1                                          |         | add <ns:element1/>                                |
		| /ns:root/ns:element2[@attr=1]                                 |         | add <ns:element2 attr="1"/>                       |
		| /ns:root/ns:element2/@attr                                    | 1       | add <ns:element2 attr="1"/>                       |
		| /ns:root/ns:element1/ns:element11                             |         | add <ns:element11/>                               |
		| /ns:root/ns:element3                                          | TEXT    | add <ns:element3>TEXT</ns:element3>               |
		| /ns:root/ns:element1[ns:element12]/ns:element13               |         | add <ns:element13/>                               |
		| //PersonList/Person[2]/Name                                   | NewName | set the second Person node's Name Text            |
		| //PersonList/Person[2]/Name/text()                            | NewName | set the second Person node's Name Text            |
		| //PersonList/Person[1]/Name(:delete)                          |         | delete this Name node                             |
		| //PersonList/Person(:add)/Name                                | NewName | alway add a new Person node                       |
		| //PersonList/Person(:insertBefore(Person[Name='Name2']))/Name | NewName | add a new Person node before Person named "Name2" |
        
		Special xmodify mark
		all special mark is start with (: and end with )
		they are not part of standard XPATH, use them only when you have to.
		| Mark                   | usage                                                                                            |
		|------------------------+--------------------------------------------------------------------------------------------------|
		| (:add)                 | always add new element (by default only add new element when not exist)                          |
		| (:delete)              | delete an element                                                                                |
		| (:insertBefore(XPATH)) | always add new element, and control the position of it, (by default only append to last element) |


		code example:
		Create new xml:

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

		result xml:
	
		<root xmlns="http://localhost">
			<element1>
				<element11/>
			</element1>
			<element2 attr="1"/>
			<element3>TEXT</element3>
		</root>
	
		Modify exist xml:
		original xml:

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


		Document document = readDocument("modify.xml");
		XModifier modifier = new XModifier(document);
		modifier.setNamespace("ns", "http://localhost");
		modifier.addModify("/ns:root/ns:element1[ns:element12]/ns:element13");
		modifier.modify();


		result xml:

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

		a new element ns:element13 is being added