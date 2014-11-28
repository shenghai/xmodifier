xmodifier
=========
		The purpose of this project is very simple:
		use XPATH to edit XML or create XML
		XPATH is used for searching XML, but sometimes you may want use it to edit/create XML:

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
		Document documentExpected = readDocument("modifyExpected.xml");
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