# MiConversation API

*H-hmph! It's not like I spent hours coding this conversation API for you or anything... b-baka!*

## What is this?

A lightweight conversation library for Nukkit plugins. As if you couldn't figure that out yourself!

## Features

- **Structured conversation flow** - I put a lot of effort into making this, so you better appreciate it!
    - Typed prompts with validation
    - Sequential prompt handling
    - Per conversation data management

- **Various prompt types** - Pick the one that suits you, not that I care which one you use:
    - `StringPrompt` - For when you need text input, obviously!
    - `NumericPrompt` - Only accepts numbers, dummy
    - `BooleanPrompt` - Yes/no questions, don't mess it up!
    - `FixedSetPrompt` - For when I don't trust you with free input
    - `MessagePrompt` - Non-input messages, in case you need to be told something

- **Conversation control** - I-it's not like I added these just for you:
    - Timeout handling with automatic cancellation
    - Escape sequences (like "exit")
    - Custom exit messages

## Usage

Basic usage:

```java
conversationService.createConversationBuilder()
    .modal(true)
    .firstPrompt(new YourPrompt())
    .withEscapeSequence("exit")
    .withTimeout(30)
    .onAbandoned(conversation -> player.sendMessage("Conversation ended."))
    .beginConversation(player);
```

## Example Survey Conversation

Here's a complete example of a server survey conversation:

```java
// In your command or wherever you want to start the conversation
conversationService.createConversationBuilder()
    .modal(true)
    .firstPrompt(new WelcomePrompt())
    .withEscapeSequence("exit")
    .withTimeout(10)
    .onAbandoned(conversation -> player.sendMessage("Conversation ended."))
    .beginConversation(player);

// Define your conversation prompts
/**
 * Welcome to prompt shown at the start of the conversation.
 */
private static class WelcomePrompt extends MessagePrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return """
            Welcome to our server survey!
            Please answer the following questions.
            Type 'exit' at any time to exit.
            """;
    }

    @Override
    protected ConversationPrompt getNextPrompt(ConversationContext context) {
        return new NamePrompt();
    }
}

/**
 * Prompt asking for the player's name.
 */
private static class NamePrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return "What should we call you? (You can use your Minecraft name or another name)";
    }

    @Override
    public ConversationPrompt acceptInput(ConversationContext context, String input) {
        // Save the name in session data
        context.setData("name", input);
        return new AgePrompt();
    }
}

/**
 * Prompt asking for the player's age range.
 */
private static class AgePrompt extends FixedSetPrompt {
    public AgePrompt() {
        super("under 18", "18-25", "26-40", "over 40", "prefer not to say");
    }

    @Override
    public String getPromptText(ConversationContext context) {
        String name = context.getData("name", String.class).orElse("Player");
        return "Thanks, " + name + "! What age range do you fall into?\n" +
            "Options: " + formatOptions();
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(ConversationContext context, String input) {
        context.setData("age", input);
        return new PlayFrequencyPrompt();
    }
}

/**
 * Prompt asking how often the player visits the server.
 */
private static class PlayFrequencyPrompt extends FixedSetPrompt {
    public PlayFrequencyPrompt() {
        super("first time", "occasionally", "regularly", "daily");
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "How often do you play on our server?\n" +
            "Options: " + formatOptions();
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(ConversationContext context, String input) {
        context.setData("frequency", input);
        return new EnjoymentRatingPrompt();
    }
}

/**
 * Prompt asking for a numerical rating.
 */
private static class EnjoymentRatingPrompt extends NumericPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return "On a scale of 1-10, how much are you enjoying your experience on our server?";
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(ConversationContext context, Number input) {
        int rating = input.intValue();

        if (rating < 1 || rating > 10) {
            return new OutOfRangePrompt(this);
        }

        context.setData("rating", rating);

        // If rating is low, ask for feedback
        return rating <= 5 ? new FeedbackPrompt() : new ConfirmationPrompt();
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return "Please enter a number between 1 and 10.";
    }
}

/**
 * Final prompt with conversation results
 */
private static class ConfirmationPrompt extends BooleanPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        String name = context.getData("name", String.class).orElse("Player");
        String age = context.getData("age", String.class).orElse("Unknown");
        String frequency = context.getData("frequency", String.class).orElse("Unknown");
        int rating = context.getData("rating", Integer.class).orElse(0);

        var summary = new StringBuilder();
        summary.append("Thank you for completing our survey! Here's a summary:\n");
        summary.append("- Name: ").append(name).append("\n");
        summary.append("- Age Range: ").append(age).append("\n");
        summary.append("- Visit Frequency: ").append(frequency).append("\n");
        summary.append("- Server Rating: ").append(rating).append("/10\n");

        context.getData("feedback", String.class).ifPresent(feedback ->
            summary.append("- Your Feedback: ").append(feedback).append("\n")
        );

        summary.append("\nWould you like to receive occasional server updates? (yes/no)");
        return summary.toString();
    }

    @Override
    protected ConversationPrompt acceptValidatedInput(ConversationContext context, boolean input) {
        context.setData("wantsUpdates", input);
        return new FarewellPrompt();
    }
}
```

## Creating Custom Prompts

J-just extend one of the base prompts! It's super easy, even for someone like you!

```java
public class MyAwesomePrompt extends StringPrompt {
    @Override
    public String getPromptText(ConversationContext context) {
        return "What's your favorite food? N-not that I'm going to cook it for you!";
    }

    @Override
    public ConversationPrompt acceptInput(ConversationContext context, String input) {
        context.setData("food", input);
        return new NextPrompt(); // Or ConversationPrompt.END_OF_CONVERSATION
    }
}
```

## Special Features

- **Conversation timeout** - For when users are too slow to respond (ugh!)
  - Automatically abandons conversations after a specified period of inactivity
  - Resets timer when the user provides input
  - Sends customizable timeout messages

- **Modal conversations** - Blocks other chat input while active, because focusing on one thing is important, b-baka!

## Cancelling Conversations

Don't think I'm doing you a favor by explaining this! There are multiple ways to end a conversation:

```java
// Add a timeout (seconds)
.withTimeout(30)

// Add a timeout with custom message
.withTimeout(30, "Time's up! I got tired of waiting for you!")

// Add an escape sequence
.withEscapeSequence("exit")

// Add an escape sequence with custom message
.withEscapeSequence("exit", "Finally! I was getting bored anyway!")
```

## Installation

Just add the JAR file to your server's `plugins` folder and as a dependency in your plugin. Is that simple enough for you?
### Using Gradle Groovy
```groovy
dependencies {
  compileOnly fileTree(dir: 'libs', include: ['*.jar'])
}
```

### Using Gradle Kotlin Script
```kotlin
dependencies {
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}
```

### Using Maven
```xml
<dependency>
    <groupId>me.millesant</groupId>
    <artifactId>MiConversation</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
    <systemPath>${project.basedir}/libs/MiConversation-1.0.0.jar</systemPath>
</dependency>
```

## Setup in Your Plugin

To use the conversation system in your plugin:

```java
public class YourPlugin extends PluginBase {

    private ConversationService conversationService;
    
    @Override
    public void onEnable() {
        // Get the conversation service
        this.conversationService = this.getServer()
            .getServiceManager()
            .getProvider(ConversationService.class)
            .getProvider();
            
        // Now you can use the conversation service
        // this.conversationService.createConversationBuilder()...
    }
}
```

*...D-don't think I'll help you if you have problems with it or anything! But I guess you can open an issue if you absolutely must...*