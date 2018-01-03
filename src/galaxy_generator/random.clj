(ns galaxy-generator.random)


(defn normally-distributed-single
  [^java.util.Random r sd mean]
  (let [u1 (.nextDouble r)
        u2 (.nextDouble r)
        x1 (Math/sqrt (* -2 (Math/log u1)))
        x2 (* 2 Math/PI u2)
        z1 (* x1 (Math/sin x2))]
    (+ mean (* z1 sd))))

