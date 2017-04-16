require_relative 'modules/CaseClassModule.rb'
require_relative 'CaseClass.rb'

class Padre
	def metodo_padre
		"SOY EL PADRE"
	end
end

module M
	def metodo_m
		"SOY DEL MIXIN M"
	end
end

case_class CC_A < Padre do
  include M
  def metodo_hijo
		"SOY EL HIJO"
	end
end