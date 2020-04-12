(defun strlen (s)
  (cond
    ((equal s "") 0)
    (T (+ 1 (strlen (subseq s 1))))))
