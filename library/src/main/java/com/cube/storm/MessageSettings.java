package com.cube.storm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cube.storm.message.lib.listener.RegisterListener;
import com.cube.storm.message.lib.receiver.MessageReceiver;

import lombok.Getter;
import lombok.Setter;

/**
 * This is the entry point class of the library. To enable the use of the library, you must instantiate
 * a new {@link com.cube.storm.MessageSettings.Builder} object in your {@link android.app.Application} singleton class.
 *
 * This class should not be directly instantiated.
 *
 * @author Callum Taylor
 * @project StormMessage
 */
public class MessageSettings
{
	/**
	 * The singleton instance of the settings
	 */
	private static MessageSettings instance;

	/**
	 * Gets the instance of the {@link com.cube.storm.MessageSettings} class
	 * Throws a {@link IllegalAccessError} if the singleton has not been instantiated properly
	 *
	 * @return The instance
	 */
	public static MessageSettings getInstance()
	{
		if (instance == null)
		{
			throw new IllegalAccessError("You must build the Message settings object first using MessageSettings$Builder");
		}

		return instance;
	}

	/**
	 * Default private constructor
	 */
	private MessageSettings(){}

	/**
	 * Project number as defined in the Google console project page under "project number"
	 */
	@Getter @Setter private String projectNumber;

	/**
	 * Callback used once the device has been registered for a push token
	 */
	@Getter @Setter private RegisterListener registerListener;

	/**
	 * The gcm receiver class used to receive messages from Storm.
	 */
	@Getter @Setter private MessageReceiver receiver;

	/**
	 * The builder class for {@link com.cube.storm.MessageSettings}. Use this to create a new {@link com.cube.storm.MessageSettings} instance
	 * with the customised properties specific for your project.
	 *
	 * Call {@link #build()} to build the settings object.
	 */
	public static class Builder
	{
		/**
		 * The temporary instance of the {@link com.cube.storm.MessageSettings} object.
		 */
		private MessageSettings construct;

		private Context context;

		/**
		 * Default constructor
		 */
		public Builder(@NonNull Context context)
		{
			this.construct = new MessageSettings();
			this.context = context.getApplicationContext();

			messageReceiver(new MessageReceiver());
		}

		/**
		 * Sets the project number from the console
		 *
		 * @param projectNumber The project number
		 *
		 * @return The builder to allow for chaining
		 */
		public Builder projectNumber(@NonNull String projectNumber)
		{
			construct.projectNumber = projectNumber;
			return this;
		}

		/**
		 * Sets the callback listener for when the device has been registered for a push token
		 *
		 * @param listener The listener to use
		 *
		 * @return The builder to allow for chaining
		 */
		public Builder registerListener(@Nullable RegisterListener listener)
		{
			construct.registerListener = listener;
			return this;
		}

		/**
		 * Sets the receiver for the module. You must also set this in your manifest for the framework
		 * to use correctly.
		 * <p/>
		 * <pre>
		 * &lt;receiver
		 *	 android:name="com.cube.storm.message.lib.receiver.MessageReceiver"
		 *	 android:permission="com.google.android.c2dm.permission.SEND"
		 * &gt;
		 *	 &lt;intent-filter&gt;
		 *	 	&lt;action android:name="com.google.android.c2dm.intent.RECEIVE" /&gt;
		 * 	 	&lt;category android:name="com.cube.storm.example" /&gt;
		 *	 &lt;/intent-filter&gt;
		 * &lt;/receiver&gt;
		 * </pre>
		 *
		 * @param receiver The receiver to use
		 *
		 * @return The builder to allow for chaining
		 */
		public Builder messageReceiver(@Nullable MessageReceiver receiver)
		{
			construct.receiver = receiver;
			return this;
		}

		/**
		 * Builds the final settings object and sets its instance. Use {@link #getInstance()} to retrieve the settings
		 * instance.
		 * <p/>
		 * This method will also register the receiver to the given context in {@link #Builder(android.content.Context)} if
		 * the receiver is not null.
		 *
		 * @return The newly set {@link com.cube.storm.MessageSettings} instance
		 */
		public MessageSettings build()
		{
			MessageSettings.instance = construct;

			if (construct.receiver != null)
			{
				construct.receiver.register(context);
			}

			return construct;
		}
	}
}