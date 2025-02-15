/* eslint-disable react-refresh/only-export-components */
import React, { createContext, useContext, useState, useCallback } from "react";
import { Device } from "mediasoup-client";
import { RtpCapabilities } from "mediasoup-client/lib/RtpParameters";

interface DeviceContextValue {
  device: Device | null;
  initializeDevice: (rtpCapabilities: RtpCapabilities) => Promise<Device>;
}

const DeviceContext = createContext<DeviceContextValue | null>(null);

export const DeviceProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [device, setDevice] = useState<Device | null>(null);

  const initializeDevice = useCallback(async (rtpCapabilities: RtpCapabilities) => {
    if (device) {
      console.log("Device already initialized");
      return device;
    }

    try {
      const newDevice = new Device();
      await newDevice.load({ routerRtpCapabilities: rtpCapabilities });
      if (newDevice.loaded) {
        console.log("Device initialized");
      } else {
        throw new Error("Failed to load device");
      }
      setDevice(newDevice);
      return newDevice;
    } catch (error) {
      console.error("Failed to initialize device", error);
      throw new Error("Device initialization failed. Check browser compatibility.");
    }
  }, [device]);


  return (
    <DeviceContext.Provider value={{ device, initializeDevice }}>
      {children}
    </DeviceContext.Provider>
  );
};

export const useDevice = () => {
  const context = useContext(DeviceContext);
  if (!context) {
    throw new Error("useDevice must be used within a DeviceProvider");
  }
  return context;
};
