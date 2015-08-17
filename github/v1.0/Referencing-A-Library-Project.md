
#### Android Setup

- In the **Package Explorer**, right-click the dependent project and select **Properties**.
- In the **Properties** window, select the Android properties group at left and locate the **Library **properties at right.
- Click **Add **to open the **Project Selection** dialog.
- From the list of available library project, select a project and click **OK**.
- When the dialog closes, click **Apply** in the **Properties** window.
- Click **OK** to close the **Properties** window.

![] (https://raw.github.com/Siminov/android-core/docs/github/v1.0/referencing_a_library_project.png "")


#### iOS Setup

- Select your **application project** to go to the project editor, then select your application target to go to the target editor.
- Select the **build phases** tab. Disclose the **Link Binary with Libraries** phase and click the **+** button in that phase. 
- Choose your static library **library_build.a** and add it to the phase. Your application will now link against your library.
- To include **library headers**, right click your application project and click **Add Files to Project**. Choose **include** folder and check all options except **Create groups**.
- Add the **-ObjC** flag to the **Other Linker Flags** build setting.
- Set **Build Active Architecture Only** to **No** in build setting.
- Add **"$(SRCROOT)/include"** to the **Header Search Paths** build setting.
 
![] (https://raw.github.com/Siminov/ios-core/docs/github/v1.0/referencing_a_library_project_1.png "")

#### Windows Setup

- In **Solution Explorer**, right-click the **References** node underneath the application, and then click **Add Reference**.
- In the **Add Reference** dialog box, select the **Projects** tab.
- **Double-click** the class library project. Class Library will appear under the **References** node for the application project.
- In **Solution Explorer**, right-click **Default.aspx** and then click **View Code**.

![] (https://raw.github.com/Siminov/windows-core/docs/github/v1.0/referencing_a_library_project.png "")
