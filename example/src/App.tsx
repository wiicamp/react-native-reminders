import React from 'react';
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
} from 'react-native';
import Reminders from '@lampn9397/react-native-reminders';

export default function App() {
  const [state, setState] = React.useState({
    title: 'Test add reminder',
    note: 'Note',
  });

  const getReminders = React.useCallback(async () => {
    try {
      const isAllow = await Reminders.requestPermission();

      if (isAllow) {
        const reminders = await Reminders.getReminders();
        console.log('requestPermission -> reminders', reminders);
      }
    } catch (error) {
      console.log(error);
    }
  }, []);

  const onChangeText = React.useCallback((text, fieldName) => {
    setState((prevState) => ({ ...prevState, [fieldName]: text }));
  }, []);

  const addReminder = React.useCallback(async () => {
    try {
      const reminderModel = {
        ...state,
        timestamp: Date.now() + 60000 * 10,
      };
      const result = await Reminders.addReminder(reminderModel);
      console.log('App -> result', result);
    } catch (error) {
      console.log(error);
    }
  }, []);

  React.useEffect(() => {
    getReminders();
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.buttonTouchable} onPress={getReminders}>
        <Text style={styles.buttonText}>Get Reminders</Text>
      </TouchableOpacity>

      <TextInput
        value={state.title}
        style={styles.textinput}
        onChangeText={(text) => onChangeText(text, 'title')}
      />
      <TextInput
        value={state.note}
        style={styles.textinput}
        onChangeText={(text) => onChangeText(text, 'note')}
      />

      <TouchableOpacity style={styles.buttonTouchable} onPress={addReminder}>
        <Text style={styles.buttonText}>Add Reminder</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    // alignItems: 'center',
    justifyContent: 'center',
  },
  buttonTouchable: {
    margin: 12,
    padding: 12,
    // borderWidth: 1,
    borderRadius: 4,
    backgroundColor: '#0984e3',
  },
  buttonText: {
    color: 'white',
    textAlign: 'center',
  },
  textinput: {
    margin: 12,
    padding: 12,
    borderWidth: 1,
    borderRadius: 4,
    borderColor: '#fd79a8',
  },
});
