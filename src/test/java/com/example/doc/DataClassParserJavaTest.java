package com.example.doc;

import com.example.doc.parsing.ClassParser;
import com.example.doc.parsing.ClassView;
import com.example.doc.parsing.FieldView;
import io.swagger.v3.oas.annotations.media.Schema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@SpringBootTest
class DataClassParserJavaTest {
	private ClassParser parser = new ClassParser();

	@Schema(description = "Комментарии к фильму")
	class JavaDataModelTest {

		@Schema(description = "Идентификатор")
		Integer id;

		@Schema(description = "Комментарий")
		List<String> text;

		GenreJava genre;

		@Schema(description = "Комментарий к жанрам")
		Collection<GenreJava> genres;

		public JavaDataModelTest(@Schema(description = "Тест жанр") GenreJava genre) {
			this.genre = genre;
		}
	}


	@Schema(description = "Информация о фильме")
	class GenreJava{
		int id;
		String name;
	}

	@Test
	void test_java_class_primitive_field(){

		FieldView actualIdParse = parser.extractClassInfo(JavaDataModelTest.class).getFields()
				.stream()
				.filter(it-> it.getName().equals("id"))
				.toList().getFirst();

		FieldView expectedIdParse = new FieldView(
				"id",
				"Integer",
				"Идентификатор",
				"",
				false,
				null,
				null);

		Assertions.assertNotNull(actualIdParse);
		Assertions.assertEquals(expectedIdParse.getName(), actualIdParse.getName());
		Assertions.assertEquals(expectedIdParse.getType(), actualIdParse.getType());
		Assertions.assertEquals(expectedIdParse.getDescription(), actualIdParse.getDescription());
		Assertions.assertEquals(expectedIdParse.getRequired(), actualIdParse.getRequired());
		Assertions.assertEquals(expectedIdParse.getClassOfEnum(), actualIdParse.getClassOfEnum());
		Assertions.assertEquals(expectedIdParse.getClassOfComposite(), actualIdParse.getClassOfComposite());

	}

	@Test
	void test_java_class_primitive_collection_field(){

		FieldView actualIdParse = parser.extractClassInfo(JavaDataModelTest.class).getFields()
				.stream()
				.filter(it-> it.getName().equals("text"))
				.toList().getFirst();

		FieldView expectedIdParse = new FieldView(
				"text",
				"[]<String>",
				"Комментарий",
				"",
				false,
				null,
				null);

		Assertions.assertNotNull(actualIdParse);
		Assertions.assertEquals(expectedIdParse.getName(), actualIdParse.getName());
		Assertions.assertEquals(expectedIdParse.getType(), actualIdParse.getType());
		Assertions.assertEquals(expectedIdParse.getDescription(), actualIdParse.getDescription());
		Assertions.assertEquals(expectedIdParse.getRequired(), actualIdParse.getRequired());
		Assertions.assertEquals(expectedIdParse.getClassOfEnum(), actualIdParse.getClassOfEnum());
		Assertions.assertEquals(expectedIdParse.getClassOfComposite(), actualIdParse.getClassOfComposite());
	}

	@Test
	void test_java_class_composite_field(){

		FieldView actualIdParse = parser.extractClassInfo(JavaDataModelTest.class).getFields()
				.stream()
				.filter(it-> it.getName().equals("genre"))
				.toList().getFirst();

		FieldView expectedIdParse = new FieldView(
				"genre",
				"GenreJava",
				"Тест жанр",
				"",
				false,
				null,
				new ClassView ("GenreJava",
						"package com.example.doc",
						"Информация о фильме",
						Collections.emptyList()));

		Assertions.assertNotNull(actualIdParse);
		Assertions.assertEquals(expectedIdParse.getName(), actualIdParse.getName());
		Assertions.assertEquals(expectedIdParse.getType(), actualIdParse.getType());
		Assertions.assertEquals(expectedIdParse.getRequired(), actualIdParse.getRequired());
		Assertions.assertEquals(expectedIdParse.getClassOfEnum(), actualIdParse.getClassOfEnum());
		Assertions.assertEquals(expectedIdParse.getClassOfComposite(), actualIdParse.getClassOfComposite());
	}

	@Test
	void test_java_class_collection_composite_field(){

		FieldView actualIdParse = parser.extractClassInfo(JavaDataModelTest.class).getFields()
				.stream()
				.filter(it -> it.getName().equals("genres"))
				.toList().getFirst();

		FieldView expectedIdParse = new FieldView(
				"genres",
				"[]<DataClassParserJavaTest$GenreJava>",
				"Комментарий к жанрам",
				"",
				false,
				null,
				new ClassView ("GenreJava",
						"package com.example.doc",
						"Информация о фильме",
						Collections.emptyList()));

		Assertions.assertNotNull(actualIdParse);
		Assertions.assertEquals(expectedIdParse.getName(), actualIdParse.getName());
		Assertions.assertEquals(expectedIdParse.getType(), actualIdParse.getType());
		Assertions.assertEquals(expectedIdParse.getDescription(), actualIdParse.getDescription());
		Assertions.assertEquals(expectedIdParse.getRequired(), actualIdParse.getRequired());
		Assertions.assertEquals(expectedIdParse.getClassOfEnum(), actualIdParse.getClassOfEnum());
		Assertions.assertEquals(expectedIdParse.getClassOfComposite(), actualIdParse.getClassOfComposite());
	}

	@Test
	void java_class_get_schema_in_constructor(){
		String actualDescription = parser.extractClassInfo(JavaDataModelTest.class).getFields()
				.stream()
				.filter(it -> it.getName().equals("genre"))
				.toList().getFirst().getDescription();

		Assertions.assertNotNull(actualDescription);
		Assertions.assertEquals("Тест жанр", actualDescription);
	}
}
