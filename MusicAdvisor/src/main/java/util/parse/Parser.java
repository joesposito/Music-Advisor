package util.parse;

import java.util.List;

public interface Parser<E> {
   List<E> parse(String response);
}
