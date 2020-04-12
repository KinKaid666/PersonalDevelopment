(case-sensitive #t)
(define substitute
    (lambda (exp env)
        (cond
 	    [(null? env) exp]
            [(null? exp) exp]
            [(atom? exp) (if (assoc exp env)
                             (cdr (assoc exp env))
                             exp)]
            [else (cons (substitute (car exp) env)
                        (substitute (cdr exp) env))])))

(define delete
    (lambda (x l)
 	(cond
            [(null? l) l]
            [else (if (eq? x (caar l))
                      (delete x (cdr l))
                      (cons (car l) (delete x (cdr l))))])))

(define occurs-in
    (lambda (x l)
        (cond
 	    [(null? l) #f]
	    [(atom? (car l)) (if (eq? x (car l))
                                 #t
                                 (occurs-in x (cdr l)))]
            [else (or (occurs-in x (car l))
                      (occurs-in x (cdr l)))])))

(define variable?
    (lambda (x)
	(and (symbol? x)
	     (let ([first-char
	             (char->integer (string-ref (symbol->string x) 0))])
               (and (>= first-char 65) (<= first-char 90))))))

(define unwind
    (lambda (env1 env2)
	(letrec ([unw
                   (lambda (e1 e2)
                     (if (null? e1)
                         e2
                         (cons (cons (caar e1) (substitute (cdr (car e1)) env2))
                               (unw (cdr e1) (delete (caar e1) e2)))))])
          (unw env1 env2))))

(define unify
    (lambda (s1 s2)
	(if (atom? s1)
	    (if (eq? s1 s2)
 		'()
		(if (variable? s1)
		    (if (occurs-in s1 s2)
			'fail
			(list (cons s1 s2)))
		    (if (variable? s2)
			(list (cons s2 s1))
			'fail)))
	(if (atom? s2)
	    (unify s2 s1)
            (let ([z1 (unify (car s1) (car s2))])
		(if (eq? z1 'fail)
		    'fail
	            (let ([g1 (substitute (cdr s1) z1)]
		          [g2 (substitute (cdr s2) z1)])
                        (let ([z2 (unify g1 g2)])
                            (if (eq? z2 'fail)
	                        'fail
				(unwind z1 z2))))))))))

