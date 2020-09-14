import { NativeModules } from 'react-native';

type AlarmType = {
  offset: Number;
  date: String;
};

type ReminderType = {
  id: String;
  title: String;
  note: String;
  alarms: Array<AlarmType>;
};

type ReminderConfig = {
  title: String;
  note: String;
  timestamp: Number;
};

type RemindersType = {
  /**
   * Request permission to read and write the reminder
   */
  requestPermission(): Promise<boolean>;
  /**
   * Get reminders from Reminders app on iOS and Calendar app on Android
   */
  getReminders(): Promise<Array<ReminderType>>;
  /**
   *
   * @param config Reminder configurations
   */
  addReminder(config: ReminderConfig): Promise<ReminderType>;
  /**
   *
   * @param id The identify of reminder
   */
  removeReminder(id: String): Promise<Boolean>;
};

const { Reminders } = NativeModules;

export default Reminders as RemindersType;
