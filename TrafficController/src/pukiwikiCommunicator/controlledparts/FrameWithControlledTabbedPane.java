/*
 * 作成日: 2005/04/25
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
package pukiwikiCommunicator.controlledparts;

import java.awt.*;

/**
 * @author yamachan
 *
 * この生成されたコメントの挿入されるテンプレートを変更するため
 * ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public interface FrameWithControlledTabbedPane {

	void changeStateOnTheTabbedPane(int id, int value);

	void exitMouseOnTheTabbedPane(int id);

	void enterMouseOnTheTabbedPane(int id);

	void sendEvent(String x);

	boolean isControlledByLocalUser();

	Color getBackground();

	void stateChangedAtTabbedPane(int id, int value);

	void mouseExitedAtTabbedPane(int id);

	void mouseEnteredAtTabbedPane(int id);


	//{{DECLARE_CONTROLS
	//}}

}