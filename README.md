# MiConversation API

*H-hmph! It's not like I spent hours coding this conversation API for you or anything... b-baka!*

## What is this?

A lightweight conversation library for Nukkit plugins. As if you couldn't figure that out yourself!

## Features

- **Structured conversation flow** - I put a lot of effort into making this, so you better appreciate it!
    - Typed prompts with validation
    - Sequential prompt handling
    - Session data management

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

```java
new ConversationBuilder(yourPlugin)
    .modal(true)
    .firstPrompt(new YourPrompt())
    .escapeSequence("exit", "Fine! Conversation over!")
    .timeout(30)
    .beginConversation(player);
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
        context.setSessionData("food", input);
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
.timeout(30)

// Add a timeout with custom message
.timeout(30, "Time's up! I got tired of waiting for you!")

// Add an escape sequence
.escapeSequence("exit")

// Add an escape sequence with custom message
.escapeSequence("exit", "Finally! I was getting bored anyway!")
```

## Installation

Just add this as a dependency in your plugin. What? You thought I'd provide JAR downloads too? Figure it out yourself!

```gradle
dependencies {
    implementation project(':MiConversation')
}
```

*...D-don't think I'll help you if you have problems with it or anything! But I guess you can open an issue if you absolutely must...*