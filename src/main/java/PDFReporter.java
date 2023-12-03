import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PDFReporter {
    static String outputFilePathGroups = "./reports/report_groups.pdf";
    private static final Logger logger = LogManager.getLogger("mainLogger");

    public static void createReportGroups(String dataFilePath) {
        try {
            JRXmlDataSource dataSource = new JRXmlDataSource(dataFilePath, "/groups/group");
            JasperReport jasperReport = JasperCompileManager.compileReport("./jrxml/report4.jrxml");
            JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dataSource);
            if (outputFilePathGroups.toLowerCase().endsWith("pdf")) {
                JRPdfExporter exporter = new JRPdfExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFilePathGroups));
                exporter.setConfiguration(new SimplePdfReportConfiguration());
                exporter.setConfiguration(new SimplePdfExporterConfiguration());
                exporter.exportReport();
            } else {
                HtmlExporter exporter = new HtmlExporter();
                exporter.setExporterInput(new SimpleExporterInput(print));
                exporter.setExporterOutput(new SimpleHtmlExporterOutput(outputFilePathGroups));
                exporter.setConfiguration(new SimpleHtmlReportConfiguration());
                exporter.setConfiguration(new SimpleHtmlExporterConfiguration());
                exporter.exportReport();
            }
            JasperViewer.viewReport(print, false);
            logger.info("Report {} successfully created", outputFilePathGroups);
        } catch (JRException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public static void createReportTours(int groupId) {
        try {
            JasperReport jasperReport = JasperCompileManager.compileReport("D:/tanki/University/OOP/LAB/Lab1/jrxml/report_tours.jrxml");

            // Set up parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("group_id",groupId);

            Connection connection = getDatabaseConnection();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            String outputFile = "./reports/report_tours.pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);

            logger.info("Report {} successfully exported",outputFile);
            connection.close();
        } catch (JRException | SQLException e) {
            logger.error("Some problem with JR: " + e.getMessage() ,e);
        }
    }

    private static Connection getDatabaseConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://localhost:3306/music_groups";
        String username = "root";
        String password = "123456";
        logger.info("Connection to DB successful");
        return DriverManager.getConnection(jdbcUrl, username, password);
    }
}
