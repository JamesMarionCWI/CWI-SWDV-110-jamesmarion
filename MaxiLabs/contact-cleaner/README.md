# Reflection

### Strings are **immutable**. Name a place in your code where forgetting that would have caused a bug.
Any place I called a string transformation method, such as using toLowerCase() in the makeUsername function in the ContactParser class.

### Why does `split("|")` behave differently from `split("\\|")`?
Because the pipe is a special character in regular expressions.

### Where did you choose `StringBuilder` instead of `+`, and why?
Anytime I was using loops and building up a string, I used StringBuilder to assist in loop performance. setCharacter is also a very useful StringBuilder method.

### Paste the output of `mvn test` (the summary line is enough).
```
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.085 s -- in edu.cwi.swdv110.contacts.ContactParserTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 20, Failures: 0, Errors: 0, Skipped: 0
```
