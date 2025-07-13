# 🔍 Dynamic Filter Engine for Spring Boot

A flexible and reusable filtering engine built on top of Spring Boot and `JpaSpecificationExecutor`, allowing dynamic query construction using simple query parameters.

## 📦 Features

- ✅ Parse filters from query string using structured syntax
- 🔧 Supports operations: `=`, `!=`, `>`, `<`, `in`, `between`, `contains`, `is_null`, etc.
- 📌 Custom annotations: `@Critiria`, `@SortParam`, `@SearchValue`
- 🔁 Dynamic joins with nested attributes (`customer.address.city`)
- 🧠 Clean clause structure: `Clause`, `ClauseOneArg`, `ClauseTwoArgs`, `ClauseArrayArgs`
- ⚙️ Plug-and-play with `JpaSpecificationExecutor`

---

## 📂 Packages Structure

```java
com.yakoub.ea.filters.clause             // Clause types and predicate logic
com.yakoub.ea.filters.enums              // Enum: Operation, Connecteur
com.yakoub.ea.filters.factory            // Value resolution (type inference)
com.yakoub.ea.filters.creator            // Join and attribute resolution
com.yakoub.ea.filters.handlerMethodArgumentResolver // Web argument resolvers
com.yakoub.ea.filters.specification      // GenericSpecification for JPA
```

---

## 🔍 Example Usage

```http
GET /api/v1/group?filter=type:=:INTERNAL&filter=name:contains:admin
GET /mytours?filter=price:between:200,800&filter=region:in:Southern
```

Each `filter` is in the format: `field:operation:value`

---

## 🧩 Sample Code: Predicate Logic

```java
public static Predicate toPredicate(Root<?> root, CriteriaBuilder cb, Clause clause) {
    ClauseOneArg oneArg = (ClauseOneArg) clause;
    Path<?> path = resolvePath(root, clause.getField());
    Object value = ValueFactory.toValue(path, oneArg.getArg());
    return cb.equal(path, value);
}
```

For operations like `between`, `in`, etc., see:
- `ClauseTwoArgs`
- `ClauseArrayArgs`

---

## 💡 Why Build Instead of Using a Library?

> I wasn’t aware of solutions like `specification-arg-resolver` when I started. Even now, building it myself gave me:

- Full control over filtering logic
- Deep understanding of Spring MVC internals
- A solution tailored to our domain model

---

## 🚀 Technologies Used

- Spring Boot (MVC + Data JPA)
- JPA Specification API
- Custom Argument Resolvers
- Lombok + Java Validation

---

## 📎 Reference Code

- 🔗 GitHub: [algeriatours - Filters](https://github.com/yakoubOuldbouchiba/algeriatours/tree/master/src/main/java/com/yakoub/ea/filters)
- 📁 Classes: `ClauseOneArg`, `ClauseTwoArgs`, `GenericSpecification`, etc.

---

## 🛠 Future Improvements

- [ ] Refactor to use strategy pattern per operation
- [ ] Better error feedback for invalid filters
- [ ] Package as a Spring Boot Starter
- [ ] Support for DTO-to-Entity field mapping with annotation:

```java
public class GroupFilterDTO {
   @FilterField("type")
   private String groupType;
   @FilterField("name")
   private String groupName;
}
```

---

## 🤝 Contributions

PRs and suggestions welcome! Feel free to fork or raise issues.

---

## 📌 Author

Built with ❤️ by [Yakoub Ouldbouchiba](https://www.linkedin.com/in/yakoubouldbouchiba)

---

## 📃 License

MIT License
