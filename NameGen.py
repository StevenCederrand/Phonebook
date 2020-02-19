import names
import xml.etree.cElementTree as ET
import random


def indent(elem, level=0):
    i = "\n" + level*"  "
    j = "\n" + (level-1)*"  "
    if len(elem):
        if not elem.text or not elem.text.strip():
            elem.text = i + "  "
        if not elem.tail or not elem.tail.strip():
            elem.tail = i
        for subelem in elem:
            indent(subelem, level+1)
        if not elem.tail or not elem.tail.strip():
            elem.tail = j
    else:
        if level and (not elem.tail or not elem.tail.strip()):
            elem.tail = j
    return elem

root = ET.Element("PHONEBOOK")
for x in range(0, 10):
    #Generate a random name
    doc = ET.SubElement(root, "CONTACT")
    ET.SubElement(doc, "NAME").text = names.get_full_name()
    #Generate a random number
    number = random.randrange(100000000, 999999999)
    ET.SubElement(doc, "WORK_NUM").text = str(number)
    #Generate a random number
    number = random.randrange(100000000, 999999999)
    ET.SubElement(doc, "HOME_NUM").text = str(number)
    pass

indent(root)
tree = ET.ElementTree(root)
tree.write("../rsc/Phonebook.xml")
