# naturaldateinput

Java library for parsing human, natural language dates, inspired by
[Chrono.js](https://github.com/wanasit/chrono/) and Todoist's smart date recognition.

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.hashadex.naturaldateinput/naturaldateinput?label=maven%20central)](https://central.sonatype.com/artifact/io.github.hashadex.naturaldateinput/naturaldateinput)

This library supports the parsing of various date formats and expressions:

* `2025-08-04` / `04.08.2025` / `8/4/2025`
* `18:00` / `6:00 pm`
* `in three days`
* `4th of August, 2025` / `August 4th, 2025` / `2025 August 4`
* `tomorrow` / `today` / `yesterday`
* `noon` / `morning` etc.
* `next friday`

Currently, the only supported languages are English and Russian.

## Installation

### Maven

```xml
<dependency>
    <groupId>io.github.hashadex.naturaldateinput</groupId>
    <artifactId>naturaldateinput</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

<!-- markdownlint-disable MD040 -->
```
implementation 'io.github.hashadex.naturaldateinput:naturaldateinput:1.0.0'
```

## Usage

> [!TIP]
> API documentation is available on [GitHub Pages](https://hashadex.github.io/naturaldateinput/apidocs/).

Simply load a pre-made `ParsingConfiguration` and use the `parse` method:

```java
// Create ParsingConfiguration and set the preferred day-month order for parsing
// ambiguous dates like 10/12/2025
ParsingConfiguration conf = new ENParsingConfiguration(DayMonthOrder.DAY_MONTH);

ParseResult result = conf.parse("Meeting tomorrow at 5 pm");

System.out.println(result.date().get()); // -> 2025-08-05
System.out.println(result.time().get()); // -> 17:00
```

A `ParsingConfiguration` is a set of multiple `Parser`s, and a `Parser` is a tool
that handles the parsing of one date/time format. You can use a single `Parser`
directly if you wish to parse only one format:

```java
ENWeekdayParser parser = new ENWeekdayParser();

Stream<ParsedComponent> results = parser.parse("Due on friday");
ParsedComponent result = results.findAny().get();

System.out.println(result.text()); // -> "on friday"
System.out.println(result.date().get()) // -> 2025-08-08
```

You can easily create your own `ParsingConfiguration`s by extending the abstract
`ParsingConfiguration` class:

```java
class CustomConfiguration extends ParsingConfiguration {
    public CustomConfiguration() {
        super(
            Set.of(
                new ENDayMonthYearParser(),
                new ENMonthDayYearParser(),
                new ENYearMonthDayParser()
            )
        );
    }
}
```
