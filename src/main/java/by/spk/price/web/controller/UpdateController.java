package by.spk.price.web.controller;

import by.spk.price.downloadCsv.DownloadCsv;
import by.spk.price.updateDb.UpdateDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/update", asyncSupported = true)
public class UpdateController extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateController.class);

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {

        LOGGER.info("updating");
        final AsyncContext asyncContext = req.startAsync();
        asyncContext.start(() -> {
            try {
                LOGGER.info("start download");
                new DownloadCsv().download();
                LOGGER.info("start update");
                new UpdateDb().update();
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                resp.setStatus(500);
                throw new IllegalStateException("Error Update Data: " + e.getMessage());
            } finally {
                LOGGER.info("complete");
                asyncContext.complete();
            }
        });

        resp.sendRedirect(getServletContext().getContextPath() + "/show?updated=ok");
    }
}
