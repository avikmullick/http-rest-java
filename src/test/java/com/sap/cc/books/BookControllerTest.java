package com.sap.cc.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

  @Autowired
  private MockMvc mockMvc;

  // Field at class level, ObjectMapper is provided by Spring Web
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private BookStorage storage;

  @BeforeEach
  public void beforeEach() {
    storage.deleteAll();
  }

  @Test
  public void getAll_noBooks_returnsEmptyList() throws Exception {
    this.mockMvc.perform(get("/api/v1/books")).andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  public void addBook_returnsCreatedBook() throws Exception {
    ResultActions resultActions=createBook(1l,"Avik");
    MockHttpServletResponse response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/books/" + returnedBookId
    assertThat(response.getHeader("location"), is("/api/v1/books/"+ String.valueOf(1L)));

  }

  public ResultActions createBook(Long id, String author) throws Exception {
    Book book=new Book();
    book.setId(id);
    book.setAuthor(author);
    // Methods for marshalling and unmarshalling objects to/from json
    String jsonBody = objectMapper.writeValueAsString(book);
    Book bookObject = objectMapper.readValue(jsonBody, Book.class);
    ResultActions resultActions=this.mockMvc.perform(post("/api/v1/books").content(jsonBody)
      .contentType(org.springframework.http.MediaType.APPLICATION_JSON));
    return resultActions;
  }

  @Test
  public void addBookAndGetSingle_returnsBook() throws Exception {
    ResultActions resultActions=createBook(1l,"Avik");
    MockHttpServletResponse response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/books/" + returnedBookId
    assertThat(response.getHeader("location"), is("/api/v1/books/"+ String.valueOf(1L)));

    this.mockMvc.perform(get(response.getHeader("location"))).andExpect(status().isOk());

  }

  @Test
  public void getSingle_noBooks_returnsNotFound() throws Exception {
    this.mockMvc.perform(get("/api/v1/books/2")).andExpect(status().isNotFound());
  }

  @Test
  public void addMultipleAndGetAll_returnsAddedBooks() throws Exception {
    ResultActions resultActions=createBook(1l,"Avik");
    MockHttpServletResponse response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/books/" + returnedBookId
    assertThat(response.getHeader("location"), is("/api/v1/books/"+ String.valueOf(1L)));

    ResultActions resultActionsGetAll=this.mockMvc.perform(get("/api/v1/books"));
    resultActionsGetAll.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));


    resultActions=createBook(2l,"Chendil");
    response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/books/" + returnedBookId
    assertThat(response.getHeader("location"), is("/api/v1/books/"+ String.valueOf(2L)));

    resultActionsGetAll=this.mockMvc.perform(get("/api/v1/books"));
    resultActionsGetAll.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)));

  }

  @Test
  public void getSingle_idLessThanOne_returnsBadRequest() throws Exception {
    this.mockMvc.perform(get("/api/v1/books/0")).andExpect(status().isBadRequest());
  }

}