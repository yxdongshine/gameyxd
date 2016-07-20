/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util.byteaccess;

import java.util.NoSuchElementException;

class ByteArrayList {
	private final Node header;
	private int firstByte;
	private int lastByte;

	protected ByteArrayList() {
		this.header = new Node(null);
	}

	public int lastByte() {
		return this.lastByte;
	}

	public int firstByte() {
		return this.firstByte;
	}

	public boolean isEmpty() {
		return (this.header.next == this.header);
	}

	public Node getFirst() {
		return this.header.getNextNode();
	}

	public Node getLast() {
		return this.header.getPreviousNode();
	}

	public void addFirst(ByteArray ba) {
		addNode(new Node(), this.header.next);
		this.firstByte -= ba.last();
	}

	public void addLast(ByteArray ba) {
		addNode(new Node(), this.header);
		this.lastByte += ba.last();
	}

	public Node removeFirst() {
		Node node = this.header.getNextNode();
		this.firstByte += node.ba.last();
		return removeNode(node);
	}

	public Node removeLast() {
		Node node = this.header.getPreviousNode();
		this.lastByte -= node.ba.last();
		return removeNode(node);
	}

	protected void addNode(Node nodeToInsert, Node insertBeforeNode) {
		nodeToInsert.next = insertBeforeNode;
		nodeToInsert.previous = insertBeforeNode.previous;
		insertBeforeNode.previous.next = nodeToInsert;
		insertBeforeNode.previous = nodeToInsert;
	}

	protected Node removeNode(Node node) {
		node.previous.next = node.next;
		node.next.previous = node.previous;
		node.removed = true;
		return node;
	}

	public class Node {
		private Node previous;
		private Node next;
		private ByteArray ba;
		private boolean removed;

		private Node() {
			this.previous = this;
			this.next = this;
		}

		private Node(ByteArray paramByteArray) {
			if (paramByteArray == null) {
				throw new IllegalArgumentException(
						"ByteArray must not be null.");
			}

			this.ba = paramByteArray;
		}

		public Node getPreviousNode() {
			if (!(hasPreviousNode())) {
				throw new NoSuchElementException();
			}
			return this.previous;
		}

		public Node getNextNode() {
			if (!(hasNextNode())) {
				throw new NoSuchElementException();
			}
			return this.next;
		}

		public boolean hasPreviousNode() {
			return (this.previous != ByteArrayList.this.header);
		}

		public boolean hasNextNode() {
			return (this.next != ByteArrayList.this.header);
		}

		public ByteArray getByteArray() {
			return this.ba;
		}

		public boolean isRemoved() {
			return this.removed;
		}
	}
}