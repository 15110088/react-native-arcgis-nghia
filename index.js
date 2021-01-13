import { NativeModules,requireNativeComponent } from 'react-native';
const COMPONENT_NAME = "MapArcgisViewAndroid";
const { ArcgisNghia } = NativeModules;

export default ArcgisNghia;
export const RNMapViewNative = requireNativeComponent(COMPONENT_NAME);
