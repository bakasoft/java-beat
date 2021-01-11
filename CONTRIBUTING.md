# CONTRIBUTING

## Examiners

An examiner 

- `getType`: Returns the identifier of the type.

### Object Examiner

Allows access to object-like structures with key-value values.

- `getKeys`: Returns the available keys of an object.
- `getValue`: Returns the value of a key in the object.

### Array Examiner

Allows access to array-like structures with indexed values.

- `getSizeOf`: Returns the size of an array.
- `getValueAt`: Returns the value of an specific index.

### Value Examiner

Decomposes a value into multiple values.

- `computeArguments`: Returns a list of values that represents the value.

### Examiners Repository

Links Java classes to Examiners.

- `findExaminer`: Returns the examiner of a class.

## Producers