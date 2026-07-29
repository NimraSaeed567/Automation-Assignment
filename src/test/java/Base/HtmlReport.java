package Base;

import java.io.FileWriter;
import java.io.IOException;

    public class HtmlReport {
        private FileWriter writer;

        public HtmlReport(String filePath) throws IOException {
            writer = new FileWriter(filePath, true);
            writer.write("<html><head><title>Test Report</title></head><body>");
            writer.write("<h1>Test Report</h1>");
        }

        public void log(String testName, String status, String details) throws IOException {
            writer.write("<h2>" + testName + "</h2>");
            writer.write("<p>Status: " + status + "</p>");
            writer.write("<p>Details: " + details + "</p>");
        }

        public void close() throws IOException {
            writer.write("</body></html>");
            writer.close();
        }
    }

