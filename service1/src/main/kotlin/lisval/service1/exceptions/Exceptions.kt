package lisval.service1.exceptions

class PersonNotFound(id: String) :
    RuntimeException("Человек с id=${id} не найден..")

class GroupNotFound(id: Long) :
    RuntimeException("Группа id=${id} не найдена..")

class EntityByFilterNotFound() :
    RuntimeException("Не найдено ни одной сущности по заданным фильтрам..")

class ValidationException :
    RuntimeException("Валидация для полей диапазона не пройдена..")