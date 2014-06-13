package org.vigour.viewer;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.freeinternals.commonlib.util.Tool;
import org.freeinternals.format.FileFormatException;
import org.freeinternals.format.classfile.ClassFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Main {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setBounds(10, 10, 1024, 768);

		final Browser browser;
		try {
			browser = new Browser(shell, SWT.NONE);
		} catch (SWTError e) {
			System.out.println("Could not instantiate Browser: "
					+ e.getMessage());
			display.dispose();
			return;
		}
		// browser.setText(createHTML());
		String curDir = System.getProperty("user.dir");
		curDir = curDir.replace('\\', '/');
		// MessageBox box = new MessageBox(shell);
		// box.setMessage(curDir);
		// box.open();
		browser.setUrl("file:///" + curDir + "/index.html");
		final BrowserFunction function = new CustomFunction(browser,
				"parseClass");

		browser.addProgressListener(new ProgressAdapter() {
			@Override
			public void completed(ProgressEvent event) {
				browser.addLocationListener(new LocationAdapter() {
					@Override
					public void changed(LocationEvent event) {
						browser.removeLocationListener(this);
						System.out
								.println("left java function-aware page, so disposed CustomFunction");
						function.dispose();
					}
				});
			}
		});

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	static class CustomFunction extends BrowserFunction {
		CustomFunction(Browser browser, String name) {
			super(browser, name);
		}

		@Override
		public Object function(Object[] arguments) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);

			JavaScriptResult ret = new JavaScriptResult();
			ret.isSuccess = false;
			try {
				if (arguments.length != 1
						|| !arguments[0].getClass().equals(String.class)) {
					return mapper.writeValueAsString(ret);
				}

				String path = (String) arguments[0];
				File file = new File(path);
				byte[] byteArray = org.freeinternals.javaclassviewer.ui.Tool.readClassFile(file);
				ret.classFile = new ClassFile(byteArray.clone());
				ret.hexStr = Tool.getByteDataHexView(byteArray, false);

				mapper.writeValue(new File("F:\\result.json"), ret);

				String x = mapper.writeValueAsString(ret);
				return x;
			} catch (IOException | FileFormatException e) {
				return "{}";
			}
		}
	}
}

class JavaScriptResult {
	public boolean isSuccess;
	public ClassFile classFile;
	public String hexStr;
}
