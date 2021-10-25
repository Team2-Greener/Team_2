/**
 * @license
 * Copyright The Closure Library Authors.
 * SPDX-License-Identifier: Apache-2.0
 */

goog.module('goog.math.MatrixTest');
goog.setTestOnly();

const Matrix = goog.require('goog.math.Matrix');
const testSuite = goog.require('goog.testing.testSuite');

testSuite({
  testConstuctorWithGoodArray() {
    const a1 = [[1, 2], [2, 3], [4, 5]];
    const m1 = new Matrix(a1);
    assertArrayEquals('1. Internal array should be the same', m1.toArray(), a1);
    assertEquals(3, m1.getSize().height);
    assertEquals(2, m1.getSize().width);

    const a2 = [[-61, 45, 123], [11112, 343, 1235]];
    const m2 = new Matrix(a2);
    assertArrayEquals('2. Internal array should be the same', m2.toArray(), a2);
    assertEquals(2, m2.getSize().height);
    assertEquals(3, m2.getSize().width);

    const a3 = [[1, 1, 1, 1], [2, 2, 2, 2], [3, 3, 3, 3], [4, 4, 4, 4]];
    const m3 = new Matrix(a3);
    assertArrayEquals('3. Internal array should be the same', m3.toArray(), a3);
    assertEquals(4, m3.getSize().height);
    assertEquals(4, m3.getSize().width);
  },

  testConstructorWithBadArray() {
    assertThrows('1. All arrays should be of equal length', () => {
      new Matrix([[1, 2, 3], [1, 2], [1]]);
    });

    assertThrows('2. All arrays should be of equal length', () => {
      new Matrix([[1, 2], [1, 2], [1, 2, 3, 4]]);
    });

    assertThrows('3. Arrays should contain only numeric values', () => {
      new Matrix([[1, 2], [1, 2], [1, 'a']]);
    });

    assertThrows('4. Arrays should contain only numeric values', () => {
      new Matrix([[1, 2], [1, 2], [1, {a: 3}]]);
    });

    assertThrows('5. Arrays should contain only numeric values', () => {
      new Matrix([[1, 2], [1, 2], [1, [1, 2, 3]]]);
    });
  },

  testConstructorWithGoodNumbers() {
    const m1 = new Matrix(2, 2);
    assertEquals('Height should be 2', 2, m1.getSize().height);
    assertEquals('Width should be 2', 2, m1.getSize().width);

    const m2 = new Matrix(4, 2);
    assertEquals('Height should be 4', 4, m2.getSize().height);
    assertEquals('Width should be 2', 2, m2.getSize().width);

    const m3 = new Matrix(4, 6);
    assertEquals('Height should be 4', 4, m3.getSize().height);
    assertEquals('Width should be 6', 6, m3.getSize().width);
  },

  testConstructorWithBadNumbers() {
    assertThrows('1. Negative argument should have errored', () => {
      new Matrix(-4, 6);
    });

    assertThrows('2. Negative argument should have errored', () => {
      new Matrix(4, -6);
    });

    assertThrows('3. Zero argument should have errored', () => {
      new Matrix(4, 0);
    });

    assertThrows('4. Zero argument should have errored', () => {
      new Matrix(0, 1);
    });
  },

  testConstructorWithMatrix() {
    const a1 = [[1, 2], [2, 3], [4, 5]];
    const m1 = new Matrix(a1);
    const m2 = new Matrix(m1);
    assertArrayEquals(
        'Internal arrays should be the same', m1.toArray(), m2.toArray());
    assertNotEquals(
        'Should be different objects', goog.getUid(m1), goog.getUid(m2));
  },

  testCreateIdentityMatrix() {
    const m1 = Matrix.createIdentityMatrix(3);
    assertArrayEquals([[1, 0, 0], [0, 1, 0], [0, 0, 1]], m1.toArray());

    const m2 = Matrix.createIdentityMatrix(4);
    assertArrayEquals(
        [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]], m2.toArray());
  },

  testIsValidArrayWithGoodArrays() {
    const fn = Matrix.isValidArray;
    assertTrue('2x2 array should be fine', fn([[1, 2], [3, 5]]));
    assertTrue('3x2 array should be fine', fn([[1, 2, 3], [3, 5, 6]]));
    assertTrue(
        '3x3 array should be fine', fn([[1, 2, 3], [3, 5, 6], [10, 10, 10]]));
    assertTrue('[[1]] should be fine', fn([[1]]));
    assertTrue('1D array should work', fn([[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]]));
    assertTrue(
        'Negs and decimals should be ok',
        fn([[0], [-4], [-10], [1.2345], [123.53]]));
    assertTrue(
        'Hex, Es and decimals are ok', fn([[0x100, 10E-2], [1.213, 213]]));
  },

  testIsValidArrayWithBadArrays() {
    const fn = Matrix.isValidArray;
    assertFalse('Arrays should have same size', fn([[1, 2], [3]]));
    assertFalse('Arrays should have same size 2', fn([[1, 2], [3, 4, 5]]));
    assertFalse('2D arrays are ok', fn([[1, 2], [3, 4], []]));
    assertFalse('Values should be numeric', fn([[1, 2], [3, 'a']]));
    assertFalse('Values can not be strings', fn([['bah'], ['foo']]));
    assertFalse('Flat array not supported', fn([1, 2, 3, 4, 5]));
  },

  testForEach() {
    const m = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    let count = 0;
    let sum = 0;
    let xs = '';
    let ys = '';

    Matrix.forEach(m, (val, x, y) => {
      count++;
      sum += val;
      xs += x;
      ys += y;
    });
    assertEquals('forEach should have visited every item', 9, count);
    assertEquals('forEach should have summed all values', 45, sum);
    assertEquals('Xs should have been visited in order', '000111222', xs);
    assertEquals('Ys should have been visited sequentially', '012012012', ys);
  },

  testMap() {
    const m1 = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    const m2 = Matrix.map(m1, (val, x, y) => val + 1);
    assertArrayEquals([[2, 3, 4], [5, 6, 7], [8, 9, 10]], m2.toArray());
  },

  testSetValueAt() {
    const m = new Matrix(3, 3);
    for (let x = 0; x < 3; x++) {
      for (let y = 0; y < 3; y++) {
        m.setValueAt(x, y, 3 * x - y);
      }
    }
    assertArrayEquals([[0, -1, -2], [3, 2, 1], [6, 5, 4]], m.toArray());
  },

  testGetValueAt() {
    const m = new Matrix([[0, -1, -2], [3, 2, 1], [6, 5, 4]]);
    for (let x = 0; x < 3; x++) {
      for (let y = 0; y < 3; y++) {
        assertEquals(
            'Value at (x, y) should equal 3x - y', 3 * x - y,
            m.getValueAt(x, y));
      }
    }
    assertNull('Out of bounds value should be null', m.getValueAt(-1, 2));
    assertNull('Out of bounds value should be null', m.getValueAt(-1, 0));
    assertNull('Out of bounds value should be null', m.getValueAt(0, 4));
  },

  testSum1() {
    const m1 = new Matrix([[1, 1, 1], [2, 2, 2], [3, 3, 3]]);
    const m2 = new Matrix([[3, 3, 3], [2, 2, 2], [1, 1, 1]]);
    assertArrayEquals(
        'Sum should be all the 4s', [[4, 4, 4], [4, 4, 4], [4, 4, 4]],
        m1.add(m2).toArray());
    assertArrayEquals(
        'Addition should be commutative', m1.add(m2).toArray(),
        m2.add(m1).toArray());
  },

  testSum2() {
    const m1 = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    const m2 = new Matrix([[-1, -2, -3], [-4, -5, -6], [-7, -8, -9]]);
    assertArrayEquals(
        'Sum should be all 0s', [[0, 0, 0], [0, 0, 0], [0, 0, 0]],
        m1.add(m2).toArray());
    assertArrayEquals(
        'Addition should be commutative', m1.add(m2).toArray(),
        m2.add(m1).toArray());
  },

  testSubtract1() {
    const m1 = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    const m2 = new Matrix([[5, 5, 5], [5, 5, 5], [5, 5, 5]]);

    assertArrayEquals(
        [[-4, -3, -2], [-1, 0, 1], [2, 3, 4]], m1.subtract(m2).toArray());
    assertArrayEquals(
        [[4, 3, 2], [1, 0, -1], [-2, -3, -4]], m2.subtract(m1).toArray());
  },

  testSubtract2() {
    const m1 = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    const m2 = new Matrix([[-1, -2, -3], [-4, -5, -6], [-7, -8, -9]]);
    assertArrayEquals(
        [[2, 4, 6], [8, 10, 12], [14, 16, 18]], m1.subtract(m2).toArray());
    assertArrayEquals(
        [[-2, -4, -6], [-8, -10, -12], [-14, -16, -18]],
        m2.subtract(m1).toArray());
  },

  testScalarMultiplication() {
    const m1 = new Matrix([[1, 1, 1], [2, 2, 2], [3, 3, 3]]);
    assertArrayEquals(
        [[2, 2, 2], [4, 4, 4], [6, 6, 6]], m1.multiply(2).toArray());
    assertArrayEquals(
        [[3, 3, 3], [6, 6, 6], [9, 9, 9]], m1.multiply(3).toArray());
    assertArrayEquals(
        [[4, 4, 4], [8, 8, 8], [12, 12, 12]], m1.multiply(4).toArray());

    const m2 = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    assertArrayEquals(
        [[2, 4, 6], [8, 10, 12], [14, 16, 18]], m2.multiply(2).toArray());
  },

  testMatrixMultiplication() {
    const m1 = new Matrix([[1, 2], [3, 4]]);
    const m2 = new Matrix([[3, 4], [5, 6]]);
    // m1 * m2
    assertArrayEquals(
        [[1 * 3 + 2 * 5, 1 * 4 + 2 * 6], [3 * 3 + 4 * 5, 3 * 4 + 4 * 6]],
        m1.multiply(m2).toArray());
    // m2 * m1 != m1 * m2
    assertArrayEquals(
        [[3 * 1 + 4 * 3, 3 * 2 + 4 * 4], [5 * 1 + 6 * 3, 5 * 2 + 6 * 4]],
        m2.multiply(m1).toArray());
    const m3 = new Matrix([[1, 2, 3, 4], [5, 6, 7, 8]]);
    const m4 = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9], [10, 11, 12]]);
    // m3 * m4
    assertArrayEquals(
        [
          [
            1 * 1 + 2 * 4 + 3 * 7 + 4 * 10,
            1 * 2 + 2 * 5 + 3 * 8 + 4 * 11,
            1 * 3 + 2 * 6 + 3 * 9 + 4 * 12,
          ],
          [
            5 * 1 + 6 * 4 + 7 * 7 + 8 * 10,
            5 * 2 + 6 * 5 + 7 * 8 + 8 * 11,
            5 * 3 + 6 * 6 + 7 * 9 + 8 * 12,
          ],
        ],
        m3.multiply(m4).toArray());
    assertThrows('Matrix dimensions should not line up.', () => {
      m4.multiply(m3);
    });
  },

  testMatrixMultiplicationIsAssociative() {
    const A = new Matrix([[1, 2], [3, 4]]);
    const B = new Matrix([[3, 4], [5, 6]]);
    const C = new Matrix([[2, 7], [9, 1]]);

    assertArrayEquals(
        'A(BC) == (AB)C', A.multiply(B.multiply(C)).toArray(),
        A.multiply(B).multiply(C).toArray());
  },

  testMatrixMultiplicationIsDistributive() {
    const A = new Matrix([[1, 2], [3, 4]]);
    const B = new Matrix([[3, 4], [5, 6]]);
    const C = new Matrix([[2, 7], [9, 1]]);

    assertArrayEquals(
        'A(B + C) = AB + AC', A.multiply(B.add(C)).toArray(),
        A.multiply(B).add(A.multiply(C)).toArray());

    assertArrayEquals(
        '(A + B)C = AC + BC', A.add(B).multiply(C).toArray(),
        A.multiply(C).add(B.multiply(C)).toArray());
  },

  testTranspose() {
    const m = new Matrix([[1, 3, 1], [0, -6, 0]]);
    const t = [[1, 0], [3, -6], [1, 0]];
    assertArrayEquals(t, m.getTranspose().toArray());
  },

  testAppendColumns() {
    const m = new Matrix([[1, 3, 2], [2, 0, 1], [5, 2, 2]]);
    const b = new Matrix([[4], [3], [1]]);
    const result = [[1, 3, 2, 4], [2, 0, 1, 3], [5, 2, 2, 1]];
    assertArrayEquals(result, m.appendColumns(b).toArray());
  },

  testAppendRows() {
    const m = new Matrix([[1, 3, 2], [2, 0, 1], [5, 2, 2]]);
    const b = new Matrix([[4, 3, 1]]);
    const result = [[1, 3, 2], [2, 0, 1], [5, 2, 2], [4, 3, 1]];
    assertArrayEquals(result, m.appendRows(b).toArray());
  },

  /** @suppress {visibility} suppression added to enable type checking */
  testSubmatrixByDeletion() {
    const m = new Matrix(
        [[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13, 14, 15, 16]]);
    const arr = [[1, 2, 3], [5, 6, 7], [13, 14, 15]];
    assertArrayEquals(arr, m.getSubmatrixByDeletion_(2, 3).toArray());
  },

  /** @suppress {visibility} suppression added to enable type checking */
  testMinor() {
    const m = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    assertEquals(-3, m.getMinor_(0, 0));
  },

  /** @suppress {visibility} suppression added to enable type checking */
  testCofactor() {
    const m = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    assertEquals(6, m.getCofactor_(0, 1));
  },

  testDeterminantForOneByOneMatrix() {
    const m = new Matrix([[3]]);
    assertEquals(3, m.getDeterminant());
  },

  testDeterminant() {
    const m = new Matrix([[1, 2, 3], [4, 5, 6], [7, 8, 9]]);
    assertEquals(0, m.getDeterminant());
  },

  /** @suppress {visibility} suppression added to enable type checking */
  testGetSubmatrix() {
    const m = new Matrix(
        [[2, -1, 0, 1, 0, 0], [-1, 2, -1, 0, 1, 0], [0, -1, 2, 0, 0, 1]]);
    const sub1 = [[2, -1, 0], [-1, 2, -1], [0, -1, 2]];
    assertArrayEquals(sub1, m.getSubmatrixByCoordinates_(0, 0, 2, 2).toArray());

    const sub2 = [[1, 0, 0], [0, 1, 0], [0, 0, 1]];
    assertArrayEquals(sub2, m.getSubmatrixByCoordinates_(0, 3).toArray());
  },

  testGetReducedRowEchelonForm() {
    const m = new Matrix(
        [[2, -1, 0, 1, 0, 0], [-1, 2, -1, 0, 1, 0], [0, -1, 2, 0, 0, 1]]);

    const expected = new Matrix([
      [1, 0, 0, .75, .5, .25], [0, 1, 0, .5, 1, .5], [0, 0, 1, .25, .5, .75]
    ]);

    assertTrue(expected.equals(m.getReducedRowEchelonForm()));
  },

  testInverse() {
    const m1 = new Matrix([[2, -1, 0], [-1, 2, -1], [0, -1, 2]]);
    const expected1 = new Matrix([[.75, .5, .25], [.5, 1, .5], [.25, .5, .75]]);
    assertTrue(expected1.equals(m1.getInverse()));

    const m2 = new Matrix([[4, 8], [7, -2]]);
    const expected2 = new Matrix([[.03125, .125], [.10936, -.0625]]);
    assertTrue(expected2.equals(m2.getInverse(), .0001));
    const m3 = new Matrix([[0, 0], [0, 0]]);
    assertNull(m3.getInverse());
    const m4 = new Matrix([[2]]);
    const expected4 = new Matrix([[.5]]);
    assertTrue(expected4.equals(m4.getInverse(), .0001));
    const m5 = new Matrix([[0]]);
    assertNull(m5.getInverse());
  },

  testEquals() {
    const a1 = new Matrix([
      [1, 0, 0, .75, .5, .25], [0, 1, 0, .5, 1, .5], [0, 0, 1, .25, .5, .75]
    ]);

    const a2 = new Matrix([
      [1, 0, 0, .75, .5, .25], [0, 1, 0, .5, 1, .5], [0, 0, 1, .25, .5, .75]
    ]);

    const a3 = new Matrix([
      [1, 0, 0, .749, .5, .25],
      [0, 1, 0, .5, 1, .5],
      [0, 0, 1, .25, .5, .75],
    ]);

    assertTrue(a1.equals(a2));
    assertTrue(a1.equals(a3, .01));
    assertFalse(a1.equals(a3, .001));
  },
});
