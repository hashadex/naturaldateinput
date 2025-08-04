# naturaldateinput

Java library for parsing human, natural language dates, inspired by
[Chrono.js](https://github.com/wanasit/chrono/) and Todoist's smart date recognition.

## Usage

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
