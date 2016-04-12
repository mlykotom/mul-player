package cz.vutbr.fit.mulplayer.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.View;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public abstract class ClickableRecyclerAdapter<VH extends ClickableRecyclerAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
	public interface OnItemClickListener {
		void onRecyclerViewItemClick(int position, int viewType);
	}

	public interface OnLongItemClickListener {
		boolean onRecyclerViewItemLongClick(int position, int viewType);
	}

	/**
	 * Clickable viewholder
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
		public OnItemClickListener mOnItemClickListener;
		public OnLongItemClickListener mOnLongItemClickListener;

		public ViewHolder(View itemView) {
			super(itemView);
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		public ViewHolder(View itemView, OnItemClickListener clickListener) {
			this(itemView);
			mOnItemClickListener = clickListener;
		}

		public ViewHolder(View itemView, OnItemClickListener clickListener, OnLongItemClickListener longClickListener) {
			this(itemView);
			mOnItemClickListener = clickListener;
			mOnLongItemClickListener = longClickListener;
		}

		public void setOnItemClickListener(OnItemClickListener listener) {
			mOnItemClickListener = listener;
		}

		public void setOnLongItemClickListener(OnLongItemClickListener listener) {
			mOnLongItemClickListener = listener;
		}

		@Override
		public void onClick(View v) {
			if (mOnItemClickListener != null) {
				mOnItemClickListener.onRecyclerViewItemClick(getAdapterPosition(), getItemViewType());
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (mOnLongItemClickListener != null && mOnLongItemClickListener.onRecyclerViewItemLongClick(getAdapterPosition(), getItemViewType())) {
				v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				return true;
			}
			return false;
		}
	}
}
