import { NativeModules } from 'react-native';

type RemindersType = {
  multiply(a: number, b: number): Promise<number>;
};

const { Reminders } = NativeModules;

export default Reminders as RemindersType;
