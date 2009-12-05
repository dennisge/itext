/*
 * This class is part of the book "iText in Action - 2nd Edition"
 * written by Bruno Lowagie (ISBN: 9781935182610)
 * For more info, go to: http://itextpdf.com/examples/
 * This example only works with the AGPL version of iText.
 */

package part2.chapter07;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfBorderArray;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfDashPattern;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.RGBColor;

import part1.chapter03.MovieTemplates;

public class TimetableAnnotations3 extends TimetableAnnotations1 {

    public static final String RESULT = "results/part2/chapter07/timetable_tickets.pdf";
    /** Path to IMDB. */
    public static final String IMDB = "http://imdb.com/title/tt%s/";
    
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        MovieTemplates.main(args);
        new TimetableAnnotations3().manipulatePdf(MovieTemplates.RESULT, RESULT);
    }

    public void manipulatePdf(String src, String dest) throws SQLException, IOException, DocumentException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        locations = PojoFactory.getLocations(connection);
        
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        int page = 1;
        Rectangle rect;
        float top;
        PdfAnnotation annotation;
        Movie movie;
        for (Date day : PojoFactory.getDays(connection)) {
            for (Screening screening : PojoFactory.getScreenings(connection, day)) {
                rect = getPosition(screening);
                movie = screening.getMovie();
                if (screening.isPress()) {
                    annotation = PdfAnnotation.createStamp(stamper.getWriter(),
                            rect, "Press only", "NotForPublicRelease");
                    annotation.setColor(RGBColor.BLACK);
                    annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                }
                else if (isSoldOut(screening)) {
                    top = reader.getPageSizeWithRotation(page).getTop();
                    annotation = PdfAnnotation.createLine(
                            stamper.getWriter(), rect, "SOLD OUT",
                            top - rect.getTop(), rect.getRight(),
                            top - rect.getBottom(), rect.getLeft());
                    annotation.setTitle(movie.getMovieTitle());
                    annotation.setColor(RGBColor.WHITE);
                    annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                    annotation.setBorderStyle(new PdfBorderDictionary(5, PdfBorderDictionary.STYLE_SOLID));
                }
                else {
                    annotation = PdfAnnotation.createSquareCircle(
                            stamper.getWriter(), rect, "Tickets available", true);
                    annotation.setTitle(movie.getMovieTitle());
                    annotation.setColor(RGBColor.BLUE);
                    annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
                    annotation.setBorder(new PdfBorderArray(0, 0, 2, new PdfDashPattern()));
                }
                stamper.addAnnotation(annotation, page);
            }
            page++;
        }
        stamper.close();
        connection.close();
    }
    
    public boolean isSoldOut(Screening screening) {
        if (screening.getMovie().getMovieTitle().startsWith("L"))
            return true;
        return false;
    }
}
