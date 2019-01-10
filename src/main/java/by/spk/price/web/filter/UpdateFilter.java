package by.spk.price.web.filter;

import javax.servlet.annotation.WebFilter;

@WebFilter(value = "/update", asyncSupported = true)
public class UpdateFilter extends AbstractFilter {

}
