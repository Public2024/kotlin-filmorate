package com.example.javafilmoratekotlin

import com.example.javafilmoratekotlin.parsing.ClassParser
import com.example.javafilmoratekotlin.parsing.MethodParser
import com.example.javafilmoratekotlin.parsing.MethodView
import com.example.javafilmoratekotlin.service.ApplicationEndpoint
import com.example.javafilmoratekotlin.service.ApplicationEndpointsFinder
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors

@SpringBootTest
class FindEndpointsTests() {

    @Test
    fun `тест_поиска_post_поинта_в_контроллере`() {

        /*       @PostMapping("/post_test")
               fun postMethod() {
               }*/

        val expected = ApplicationEndpoint(
             type = "POST",
             path = "/test/post_test",
             method = MethodView(
                  name = "postMethod",
                  description = null,
                  summary = null,

                  parameters = emptyList(),
                  result = null
             )
        )

        val actual = ApplicationEndpointsFinder(MethodParser(ClassParser())).findAllEndpoints()
             .find { it.path == "/test/post_test" }
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_поиска_get_поинта_в_контроллере`() {

 /*       @GetMapping("/get_test")
        fun getMethod() {
        }*/

        val expected = ApplicationEndpoint(
             type = "GET",
             path = "/test/get_test",
             method = MethodView(
                  name = "getMethod",
                  description = null,
                  summary = null,
                  parameters = emptyList(),
                  result = null
             )
        )

        val actual = ApplicationEndpointsFinder(MethodParser(ClassParser())).findAllEndpoints()
             .find { it.path == "/test/get_test" }
        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `тест_поиска_put_поинта_в_контроллере`() {

  /*      @PutMapping("/put_test")
        fun putMethod() {
        }*/

        val expected = ApplicationEndpoint(
             type = "PUT",
             path = "/test/put_test",
             method = MethodView(
                  name = "putMethod",
                  description = null,
                  summary = null,
                  parameters = emptyList(),
                  result = null
             )
        )

        val actual = ApplicationEndpointsFinder(MethodParser(ClassParser())).findAllEndpoints()
             .find { it.path == "/test/put_test" }
        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `тест_поиска_delete_поинта_в_контроллере`() {

/*          @DeleteMapping("/delete_test")
        fun deleteMethod() {
        }*/

        val expected = ApplicationEndpoint(
             type = "DELETE",
             path = "/test/delete_test",
             method = MethodView(
                  name = "deleteMethod",
                  description = null,
                  summary = null,
                  parameters = emptyList(),
                  result = null
             )
        )

        val actual = ApplicationEndpointsFinder(MethodParser(ClassParser())).findAllEndpoints()
             .find { it.path == "/test/delete_test" }
        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `тест_поиска_RequestMapping_post_поинта_в_контроллере`() {

 /*              @RequestMapping(method = [RequestMethod.POST], path = ["/request_post"])
        fun requestPost() {
        */

        val expected = ApplicationEndpoint(
             type = "POST",
             path = "/test/request_post",
             method = MethodView(
                  name = "requestPost",
                  description = null,
                  summary = null,
                  parameters = emptyList(),
                  result = null
             )
        )

        val actual = ApplicationEndpointsFinder(MethodParser(ClassParser())).findAllEndpoints()
             .find { it.path == "/test/request_post" }
        AssertionErrors.assertEquals("Pass", expected, actual)
    }

    @Test
    fun `тест_поиска_RequestMapping_get_поинта_в_контроллере`() {

/*        @RequestMapping(method = [RequestMethod.GET], path = ["/request_get"])
        fun requestGet() {
        }*/

        val expected = ApplicationEndpoint(
             type = "GET",
             path = "/test/request_get",
             method = MethodView(
                  name = "requestGet",
                  description = null,
                  summary = null,
                  parameters = emptyList(),
                  result = null
             )
        )

        val actual =ApplicationEndpointsFinder(MethodParser(ClassParser())).findAllEndpoints()
             .find { it.path == "/test/request_get" }
        AssertionErrors.assertEquals("Pass", expected, actual)

    }

    @Test
    fun `тест_поиска_RequestMapping_delete_поинта_в_контроллере`() {

 /*               @RequestMapping(method = [RequestMethod.DELETE], path = ["/request_delete"])
        fun requestDelete() {
        }*/

        val expected = ApplicationEndpoint(
             type = "DELETE",
             path = "/test/request_delete",
             method = MethodView(
                  name = "requestDelete",
                  description = "",
                  summary = "",
                  parameters = emptyList(),
                  result = null
             )
        )
    }


}