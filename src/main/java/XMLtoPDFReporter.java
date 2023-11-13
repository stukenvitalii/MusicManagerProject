import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JasperViewer;
public class XMLtoPDFReporter{
        static String outputFilePath = "./reports/report.html";

        public void createReport(String dataFilePath) throws Exception
        {
            try {
                JRXmlDataSource dataSource = new JRXmlDataSource(dataFilePath, "/groups/group");
                JasperReport jasperReport = JasperCompileManager.compileReport("./jrxml/report4.jrxml");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dataSource);
                if (outputFilePath.toLowerCase().endsWith("html")) {
                    JRPdfExporter exporter = new JRPdfExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFilePath));
                    exporter.setConfiguration(new SimplePdfReportConfiguration());
                    exporter.setConfiguration(new SimplePdfExporterConfiguration());
                    exporter.exportReport();
                } else{
                    HtmlExporter exporter = new HtmlExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));
                    exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputFilePath));
                    exporter.setConfiguration(new SimpleHtmlReportConfiguration());
                    exporter.setConfiguration(new SimpleHtmlExporterConfiguration());
                    exporter.exportReport();
                }
                JasperViewer.viewReport(print, false);
            } catch (JRException e) {
                e.printStackTrace();
            }
        }
    }
