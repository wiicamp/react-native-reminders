# React Native Reminders
[![NPM version](https://img.shields.io/npm/v/react-native-easy-select)](https://www.npmjs.com/package/react-native-easy-select)
[![License](https://img.shields.io/npm/l/react-native-easy-select)](https://github.com/havinhthai/react-native-easy-select/blob/master/LICENSE)
![Demo](https://reactjs.org/logo-og.png)

React Native Reminders is a modern, well-supported, and cross-platform Reminders for React Native

## Installation

```sh
yarn add @wiicamp/react-native-reminders
```
Or
```sh
npm install @wiicamp/react-native-reminders
```

#### Using React Native >= 0.60
Linking the package manually is not required anymore with [Autolinking](https://github.com/react-native-community/cli/blob/master/docs/autolinking.md).

- **iOS Platform:**

  `$ npx pod-install` # CocoaPods on iOS needs this extra step

#### Using React Native < 0.60

You then need to link the native parts of the library for the platforms you are using. The easiest way to link the library is using the CLI tool by running this command from the root of your project:

```
react-native link @wiicamp/react-native-reminders
```

### Permissions

**iOS**

The user's permission is required in order to access the Reminders on devices running iOS 10 or later. Add the `NSRemindersUsageDescription` key in your `Info.plist` with a string that describes how your app will use this data. This key will appear as `Privacy - Photo Library Usage Description` in Xcode. If you try to access Reminders without this permission, your app will exit.

**Android**

Permission is required to read and write to the Calendar.

```xml
<manifest>
...
<uses-permission android:name="android.permission.READ_CALENDAR"/>
<uses-permission android:name="android.permission.WRITE_CALENDAR"/>
...
<application>
```

Then you have to explicitly ask for the permission

## Usage

```js
import Reminders from "@wiicamp/react-native-reminders";

// Request permission
Reminders.requestPermission();

// Get reminders
Reminders.getReminders();

// Add reminder
Reminders.addReminder({
  title: 'Wake-up reminder',
  note: 'Wake-up and have breakfast!',
});
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
