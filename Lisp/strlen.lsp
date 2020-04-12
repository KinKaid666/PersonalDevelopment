(defun strlen-itr (s)
  (setq i -1)
    (loop
      (setq i (+ i 1))
      (when (equal s (subseq s 0 i)) (return i))))
