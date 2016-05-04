package cz.vutbr.fit.mulplayer.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public abstract class ClickableRecyclerAdapter<VH extends ClickableRecyclerAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
	protected LayoutInflater mInflater;
	protected OnItemClickListener mOnItemClickListener;
	protected OnLongItemClickListener mOnLongItemClickListener;

	public ClickableRecyclerAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	public interface OnItemClickListener {
		void onRecyclerViewItemClick(ViewHolder holder, int position, int viewType);
	}

	public interface OnLongItemClickListener {
		boolean onRecyclerViewItemLongClick(ViewHolder holder, int position, int viewType);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	public void setOnLongItemClickListener(OnLongItemClickListener listener) {
		mOnLongItemClickListener = listener;
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
				mOnItemClickListener.onRecyclerViewItemClick(this, getAdapterPosition(), getItemViewType());
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (mOnLongItemClickListener != null && mOnLongItemClickListener.onRecyclerViewItemLongClick(this, getAdapterPosition(), getItemViewType())) {
				v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				return true;
			}
			return false;
		}
	}
}
