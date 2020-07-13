package jm;

import java.util.List;

public interface NewsService {

    List<News> getAllNews();

    News get(Long id);

    void create(News news);

    void update(News news);

    void delete(Long id);

    News getNewsByName(String name);

}
