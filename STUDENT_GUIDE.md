# Эффективная работа с IDE

Используйте [shortcut-ы](https://blog.jetbrains.com/idea/2020/03/top-15-intellij-idea-shortcuts/)

# Организация Кода

- Называйте пулл реквест Homework + ее номер.

- В одном пулл реквесте должен присутствовать код по соответствующей домашней работе, либо по нескольким, следующим подряд.

# Типичные Ошибки

## Общие Правила

### Выбирайте отражающие суть названия

В идеале по названию класса/метода должно быть понятно, что он делает. Чем более конкретно — тем лучше.

**Нет**

```java
boolean someAct()
boolean isSomeAct()
void manage(SmartHome home)
SmartHome read()
interface EventHandling
```

**Да**

```java
boolean isClosed() // В классе с состоянием двери
void processEvent(Event event)
```

Предлагается две стратегии для названия классов с имплементаций интерфейсов:

1. Имплементация представляет частный случай — название частного случая + название интерфейса, например `interface HomeReader, class FileHomeReader, class DatabaseHomeReader`.

2. Имплементация является стандартной/единственной — название интерфейса + постфикс Impl, например `interface EventLoopProcessor, class EventLoopProcessorImpl`.

### Называйте методы глаголом с объектом [camelCase](https://google.github.io/styleguide/javaguide.html#s5.2.3-method-names)

**Нет**

```java
public void Execute()
```

**Да**

```java
public void executeSomething()
```

### Называйте классы существительными [UpperCamelCase](https://google.github.io/styleguide/javaguide.html#s5.2.2-class-names)

**Нет**

```java
class ReadFile
class ReadFromFile
class CheckIsHall
```

**Да**

```java
class FileReader
class HallChecker
```

### Называйте пакеты [строчными](https://google.github.io/styleguide/javaguide.html#s5.2.1-package-names) буквами

**Нет**

```java
package ru.sbt.mipt.oop.home.SmartHome;
```

### Не используйте статические методы в бизнес логике

Они хорошо применяются в объектах без состояния. Например, в утильных классах.

В такой ситуации лучше создать интерфейс и пользоваться им вместо вызова статического метода конкретного класса. Это позволит в будущем лекго заменить данную имплементацию ([OCP](https://drive.google.com/file/d/0BwhCYaYDn8EgN2M5MTkwM2EtNWFkZC00ZTI3LWFjZTUtNTFhZGZiYmUzODc1/view)).

**Нет**

```java
public class SpecificEventCreator {
    public static Event getNextEvent() {
        ...
    }
}

public class EventCreatorConsumer {
    public void needToUseEvent() {
        Event event = SpecificEventCreator.getNextEvent();
        ...
    }
}
```

**Да**

```java
public interface EventCreator {
    public Event getNextEvent();
}

public class EventCreatorImpl implements EventCreator {
    public Event getNextEvent() {
        ...
    }
}

public class EventCreatorConsumer {
    private final EventCreator eventCreator;

    public EventCreatorConsumer(EventCreator eventCreator) {
        this.eventCreator = eventCreator;
    }

    public void needToUseEvent() {
        Event event = eventCreator.getNextEvent();
        ...
    }
}
```

### Передавайте зависимости снаружи

Делайте зависимости полями в классе.

**Нет**

```java
public class Application {
    public void run() {
        Event event = getEvent();
        new MyEventProcessor().processEvent(event);
    }
}
```

**Да**

```java
public class Application {
    private final EventProcessor processor;
    
    public Application(EventProcessor processor) {
        this.processor = processor;
    }

    public void run() {
        Event event = getEvent();
        processor.processEvent(event);
    }
}
```

Не надо создавать специальные классы, создающие коллекции объектов. Коллекции можно создавать в конфигурационном коде и сохранять в поле потребляющего класса.

**Нет**

```java
List<EventHandler> handlers = new HandlerCreator().constructHandlers();
```

**Да**

```java
List<EventHandler> handlers = Arrays.asList(new DoorEventHandler(), ...);
```

См. [asList(T...)](https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#asList(T...))

### Сделите за видимостью методов

Помните про инкапсуляцию, не нужно давать лишний доступ к внутренним переменным/методам — клиенты должны пользоваться публичным API.

Например, пользователи изменяют состояние дома посредством отправки событий. Не надо давать им возможность включать/выключать свет в доме напрямую.

**Нет**

```java
...
public void handleEvent(Event event) {
    ...
    turnOffLight();
}

public void turnOffLight() {
    ...
}
```

**Да**

```java
...
public void handleEvent(Event event) {
    ...
    turnOffLight();
}

private void turnOffLight() {
    ...
}
```

### Конфигурируйте компоненты в main или DI фреймворке

Объекты с бизнес-логикой нужно создавать вместе с зависимостями в конфигурационном коде. Это может быть `public static void main(String[] args)`, либо конфигурации выбранного [DI фреймворка](https://www.martinfowler.com/articles/injection.html). Для этого хорошо подходят классы, у которых зависимости объявлены [аргументами конструктора](#передавайте-зависимости-снаружи).

Не надо использовать отдельные Java `enum` классы для этой цели. Их вызовы в коде тяжело менять, искать.

**Нет**

```java
public enum EventProcessors {
    DOOR_EVENT_PROCESSOR {
        @Override
        public EventProcessor createProcessor() {
            return new DoorEventProcessor();
        }
    },

    LIGHT_EVENT_PROCESSOR {
        @Override
        public EventProcessor createProcessor() {
            return new LightEventProcessor();
        }
    };

    public abstract EventProcessor createProcessor();
}
```

### Используйте интерфейсы вместо абстрактных классов

Пункт 20 из [Effective Java](https://www.amazon.com/gp/product/0134685997) говорит об превосходстве интерфейса над абстрактным классов для описания абстракции. Придерживайтесь его.

**Нет**

```java
public abstract class CommandSender {
    public abstract void sendCommand(Command command);
}
```

**Да**

```java
public interface CommandSender {
    void sendCommand(Command command);
}
```

## Задание 1

### Оставляйте в репозитории только файлы с кодом

Часто под контроль версий кода попадает скомпилированный редактором байткод (в IntelliJ Idea это директория`target/classes`). Добавьте соответствующее исключение в `.gitignore`.

### Не давайте абстракции «течь»

От англ. [leaky abstraction](https://www.joelonsoftware.com/2002/11/11/the-law-of-leaky-abstractions/). Детали имплементации не должны быть отражены в публичном API. Распространенная ошибка — добавление классов специфических исключений в сигнатуру методов интерфейса. Другим имплементациям придется работать с этими нерелевантными для них исключениями.

### Разделяйте изменение объектов и сценарии умного дома

Помните про [SRP](https://drive.google.com/file/d/0ByOwmqah_nuGNHEtcU5OekdDMkk/view). Создайте по классу на обработку события открытия/закрытия двери и обработке сценария выключения света в доме.

### Делегируйте обработку событий отдельным классам

Если класс события сам занимается его обработкой — это нарушение [SRP](https://drive.google.com/file/d/0ByOwmqah_nuGNHEtcU5OekdDMkk/view). В будущем будет появляться необходимость добавлять больше обработчиков на одни и те же события.

### Перенесите логику цикла обработки событий в отдельный класс

Здесь речь идет про логику по запуску генерации событий из внешнего источника и делегации обработки.

## Задание 2

Задача — уйти от внешней итерации и преместить эту ответственность в объекты дома.

Что должен делать `Actionable`? Он должен принимать инстанс некого `Action` и применять его к своей внутренней структуре, которую никто кроме него не знает. Это позволяет нам легко ее менять, не затрагивая при этом других клиентов.

Что должен делать `Action`? Он должен принимать инстанс некого `HomeComponent` и делать с ним заданное действие. Это можно реализовать с помощью лямбд Java.

Кто создает `Action`? Обработчики событий.

### Имплементируйте внутренний итератор

Разберитесь, в чем [разница](https://stackoverflow.com/a/224675) между **внутренним** и **внешним** итератором. Последний использовать в данном задании нельзя, поэтому не надо имплементировать [Iterable](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html).

### Закройте доступ к внутренним данным

Наряду с советами по [дизайну API](#cделите-за-видимостью-методов) учитывайте, что после добавления логики по совершению действий над объектами дома посредством внешнего итератора вам не нужно давать доступ к некоторым объектам дома (и тем более возможность их изменять через сеттеры). Например, прямой доступ к комнатам из дома не нужен, поскольку все действия теперь можно делать через `Actionable` API. Это позволяет отвязаться от внутренней структуры дома. Теперь легко при необходимости добавлять новый уровень группировки объектов. Например, объединить комнаты в этаж.

### Пользуйтесь текущей иерархией объектов дома

Не стоит выдумывать новые иерархии (`Interior`, `Premise`, ...) **только для группировки** других объектов. Этого не требуется в задании. Здесь лишняя complexity не приносит пользы, зато усложняет чтение и восприятие кода.

### Придерживайтесь предложенным структурам Action и Actionable

Эти две абстракции, описанные в задании, максимально простые и, благодаря своей абстрактности, позволяют решать широкий спектр задач. Помните про [SRP](https://drive.google.com/file/d/0ByOwmqah_nuGNHEtcU5OekdDMkk/view). Для выполнения этого задания вам не нужно ничего в них добавлять — только имплементировать.

### Итерируйтесь по всем составляющим

В имплементации [внутреннего итератора](#имплементируйте-внутренний-итератор) внутри [композитного объекта](https://refactoring.guru/ru/design-patterns/composite) не забывайте выполнять заданное действие над всеми внутренними объектами.

## Задание 3

Сигнализация должна работать по приницпу сейфа в отеле:

1. Создается без пароля
1. При активации задается пароль
1. Для деактивации требуется вести пароль из предыдущего шага

Также добавьте возможность деактивировать сигнализацию с помощью специального события.

### Сделайте отправку максимум одного смс из декоратора

Декоратор должен прерывать обработку всех событий **раз и до корректного ввода кода** деактивации сигнализации. При этом смс-оповещение должно отправляться **всего один раз**, а не для каждого обработчика.

## Задание 4

### Используйте фабрики для конвертации событий внешней бибилотеки

Не используйте switch по строковым названиям событий для конвертации. Фабрики можно объединить через цепочку ответственности, либо через маппинг идентификаторов внешних событий на фабрики.

### Объявляйте каждого обработчика отдельным бином

Например

```java
@Bean
EventProcessor someEventProcessor() {
    return new SomeEventProcessor();
}

@Bean
...
```

Далее в бине со сторонним `SensorEventsManager`, где они все нужны, попрость `Spring` собрать все бины из конфигурации типа `EventHandler` автоматически:

```java
@Bean
SensorEventsManager sensorEventsManager(Collection<EventProcessor> eventProcessors) {
    ...
}
```

Таким образом мы явно не харкодим обработчики в одном месте и легко выключаем любой при необходимости. Можно раскидать эти бины по разным конфигурациям.

### Не объявляйте бины в конфигурации дополнительно компонентами

Объявляйте бин либо в конфигурационном классе, аннотированном [`@Configuration`](https://docs.spring.io/spring-framework/docs/5.2.3.RELEASE/javadoc-api/org/springframework/context/annotation/Configuration.html) через метод с аннотацией [`@Bean`](https://docs.spring.io/spring-framework/docs/5.2.3.RELEASE/javadoc-api/org/springframework/context/annotation/Bean.html), либо компонентом с аннотацией [`@Component`](https://docs.spring.io/spring-framework/docs/5.2.3.RELEASE/javadoc-api/org/springframework/stereotype/Component.html). Это два взаимно исключающих способа объявления бинов. Для активации поиска и регистрации последних необходимо активировать [сканирование](https://docs.spring.io/spring/docs/5.2.3.RELEASE/javadoc-api/org/springframework/context/annotation/ComponentScan.html).

## Задание 5

- Создайте бины для `RemoteControlRegistry`, имплементации `RemoteControl` и классов команд
- Зарегистрируйте в имплементации `RemoteControl` кнопки с командами как в задании (лучше все сделать внутри конфигурации)
- Зарегистрируйте имплементацию `RemoteControl` в `RemoteControlRegistry`.
- Добавьте тесты по имплементации `RemoteControl` и командам.
