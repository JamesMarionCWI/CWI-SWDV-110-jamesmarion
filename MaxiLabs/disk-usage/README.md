# Reflection

### Copy your race results into a table. Each time n doubles, about how many times longer does selection sort take? Merge sort? Explain how those ratios match O(n²) and O(n log n).
| n     | selection ms | merge ms |
| ----- | ------------ | -------- |
| 1000  | 1.3          | 0.2      |
| 2000  | 5.2          | 0.5      |
| 4000  | 27.4         | 1.1      |
| 8000  | 107.9        | 2.2      |
| 16000 | 438.3        | 5.1      |

Selection sort takes about 4-5x longer each time n doubles. Merge sort takes a little more than 2x longer each time n doubles.

The total number of comparisons in selection sort grows approximately by the square of the input size since each index triggers yet another linear search through the array.

The total number of comparisons in merge sort grows at about the same rate as the input growth factor, but because there's an additional layer of splitting in half and merging it back when the input size is doubled, the amount of work being done is slightly more than a simple linear algorithm.

### What is the Big-O of totalSize in terms of f, the number of files and folders? Why?
The Big-O for totalSize in particular is a simple linear algorithm O(f). We are not re-traversing over the files and folders that we have already counted, but we must still traverse over each file and folder once. However, sortedChildren may end up introducing additional work depending on the sorting algorithm used.

### Binary search needs a sorted list. If you only need to search once, is it worth sorting first? What if you search 1,000 times?
For an extremely small number of searches (such as 1), it is generally not worth it to sort the list first since the amount of work necessary to sort will vastly overtake the benefit of a binary search over another search algorithm such as a linear search. Searching 1000 times would definitely benefit from sorting first.

### What would happen if maxDepth had no base case? What error would Java throw, and why?
maxDepth is a bit of a bad example since it's a bit hard to imagine that one without a base case with how I chose to write it - it would simply be a missing return error on compile. But in general for recursive methods without base cases, a StackOverflowException may be common due to each recursive method call adding to the stack and being unable to be taken off the stack until a base case starts collapsing those methods back down from the stack, which will never happen due to having no base case.

### Name one more real-world problem (not folders) that has a recursive structure.
A family tree with all of the descendents of any given branch of the family.
