/* ###
 * IP: GHIDRA
 * REVIEWED: YES
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.framework.task;

import ghidra.util.SystemUtilities;
import ghidra.util.exception.CancelledException;

/**
 * Class to represent the result state of a GTask, such as whether it was cancelled or an exception
 * happened.
 */
public class GTaskResult {

	private Exception exception;
	private int priority;
	private String description;
	private String groupDescription;
	private Integer transactionID;

	/**
	 * Constructs a GTaskResult for a completed GTask with an optional exception.
	 * @param group The GTaskGroup that the completed GTask belonged to.
	 * @param task the GScheduledTask which contains the actual GTask that has completed.
	 * @param e optional exception recorded if an exception occurred while processing the task.  If
	 * the task was cancelled, there should be a CancelledException passed in here.
	 * @param transactionID The transaction id for the transaction that was open when the task was
	 * executed.  Used by the results GUI to indicate when transactions are opened and closed between
	 * tasks.
	 */
	public GTaskResult(GTaskGroup group, GScheduledTask task, Exception e, Integer transactionID) {
		this.priority = task.getPriority();
		this.description = task.getDescription();
		this.groupDescription = task.getGroup().getDescription();
		this.exception = e;
		this.transactionID = transactionID;
	}

	/**
	 * Returns a description of the task that was run.
	 * @return a description of the task that was run.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns true if the task for this result was cancelled.
	 * @return true if the task for this result was cancelled.
	 */
	public boolean wasCancelled() {
		return exception instanceof CancelledException;
	}

	/**
	 * Returns the exception generated by the task, or null if no exception was generated.  If 
	 * the task was cancelled, this will return a CancelledException.
	 * 
	 * @return the exception generated by this task or null.
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * Returns the priority at which the task was run within its group.
	 * @return the priority at which the task was run within its group.
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Returns the description for the group for which this task belonged.
	 * @return the description for the group for which this task belonged.
	 */
	public String getGroupDescription() {
		return groupDescription;
	}

	/**
	 * Returns true if the task represented by this result was executed in the same transaction
	 * as the task represented by the given GTaskResult.
	 * @param result the result to check if it was executed in the same transaction as this task
	 * result.
	 * @return true if same transaction.
	 * 
	 */
	public boolean hasSameTransaction(GTaskResult result) {
		if (result == null) {
			return false;
		}
		return SystemUtilities.isEqual(transactionID, result.transactionID);
	}

	@Override
	public String toString() {
		return description;
	}

}
