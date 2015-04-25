package frontEnd;

import java.io.File;
import java.io.IOException;

import org.jopendocument.dom.ODPackage;
import org.jopendocument.dom.ODSingleXMLDocument;
import org.jopendocument.dom.OOUtils;



public class OdfTest
{
	public static void main(String[] args) throws IOException
	{
		ODPackage p = new ODPackage(new File("/home/nalamchaitanya/test.ods")); 
		ODSingleXMLDocument doc = p.toSingle();
		/*OpenDocument test = new OpenDocument();
		test.loadFrom("/home/nalamchaitanya/test.ods");*/
		File outFile = new File("/home/nalamchaitanya/test.odt");
		//OOUtils.open(outFile);
		String str = "chaitanya";
		System.out.println(str + 'a');
		/*final JFrame f = new JFrame("Invoice Viewer");
		//ODSViewerPanel viewer = new ODSViewerPanel(test);
		//f.add(viewer);
		 f.pack();
		 f.setVisible(true);*/
	}
}
