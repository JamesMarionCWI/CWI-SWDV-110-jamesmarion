# Reflection

### Pick one method in `MusicStats` and rewrite it with a `for` loop in your README. Which version is easier to read? Which is easier to change?
Stream:
```java
public static long totalMinutes(List<Play> plays, Predicate<Play> filter) {
    // filter, mapToInt, sum
    return plays.stream().filter(filter).mapToInt(Play::seconds).sum() / 60;
}
```
For:
```java
public static long totalMinutes(List<Play> plays, Predicate<Play> filter) {
    int sum = 0;
    for(Play play : plays) {
        if(filter.test(play)) {
            sum += play.seconds();
        }
    }
    return sum / 60;
}
```
As someone who is very used to both coding styles, I generally prefer the longer explicitness of the foreach loop. It can be easier to get wrong if you're not careful, but to me, it is both easier to read and generally easier to change as well. For example, suppose that I want to accumulate multiple different values using the same loop (to avoid iterating over the list multiple times). e.g. getting both the median and the average. Because of how terminal operations work, I can't exactly re-use the same stream and get more data out of that stream, I can only `sum`. I'd have to either put the stream into an intermediate list via `toList` and loop over that multiple times, or end on a `forEach` instead, and then I'm right back to writing my own summation handler on top of whatever additional custom behavior I want. A combined approach using `forEach` like that *can* work fantastic for simpler operations, and it would also avoid the pitfalls of needing to deal with much more advanced behavior such as what the collect method is capable of. But it's certainly a balancing act in my view.

### `PlayFormatter` and `Function<Play, String>` have the same shape. Why might a team still create its own functional interface?
Our lovely programmers who get woken up at 2am to solve issues in production really appreciate having consistent and readable code. The two types may have the same shape, but PlayFormatter is much more indicative of its purpose within the codebase, and thus it is much easier to use it in the correct contexts (and also much easier to avoid accidentally using other functions that use a `Play -> String` type signature for other purposes than formatting).

### Why must the list passed to `PlayCleaner.clean` be an `ArrayList` and not the result of `List.of(...)` or `stream().toList()`?
We needed a child type of list that can have its members be changed. ArrayList fits the bill.

### A stream does nothing until a *terminal* operation runs. Name three terminal operations you used.
Some of the terminal operations I used include .collect, .toList, .sum, and .max.
