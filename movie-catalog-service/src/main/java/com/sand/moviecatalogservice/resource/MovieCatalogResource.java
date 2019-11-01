package com.sand.moviecatalogservice.resource;

import java.util.List;
import java.util.stream.Collectors;

import com.sand.moviecatalogservice.model.CatalogItem;
import com.sand.moviecatalogservice.model.Movie;
import com.sand.moviecatalogservice.model.UserRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource
{
  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping("/{userId}")
  public List<CatalogItem> getCatalog(@PathVariable("userId") String userId)
  {
    UserRating userRating = restTemplate
        .getForObject("http://movie-rating-service/ratingsdata/users/" + userId, UserRating.class);

    return userRating.getRatings().stream().map(rating -> {
      Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
      return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }).collect(Collectors.toList());
  }
}
